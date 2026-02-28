package com.example.sounds.ui

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sounds.data.repository.SoundsRepository
import com.example.sounds.data.models.Playlist
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.toPlaylist
import com.example.sounds.data.models.toSong
import com.example.sounds.data.repository.SyncManager
import com.example.sounds.player.MusicForegroundService
import com.example.sounds.player.PlaybackActions
import com.example.sounds.player.QueueManager
import com.example.sounds.player.SongPlayer
import com.example.sounds.playlist.AddTracksManager
import com.example.sounds.prefDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.WhileSubscribed
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// TODO clean up view model
class SongViewModel(
    private val app: Application,
    private val repository: SoundsRepository,
): AndroidViewModel(app) {

    private val HOMESCREEN_CUR_PAGE = intPreferencesKey("home_page")

    val savedHomePageScreen: StateFlow<Int> = app.prefDataStore.data
        .map{ it[HOMESCREEN_CUR_PAGE] ?: 0 }
        .stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            -1
        )

    fun saveHomeScreenCurrentPage(page: Int) {
        viewModelScope.launch {
            app.prefDataStore.edit {
                it[HOMESCREEN_CUR_PAGE] = page
            }
        }
    }

    private val songPlayer = SongPlayer(viewModelScope, app)
    val playerState = songPlayer.playerState
    private val queueManager = QueueManager(
        viewModelScope,
        getApplication<Application>().prefDataStore
    )
    val songQueue = queueManager.queueOfSongs

    val currentSong = queueManager.currentSong
    val previousSong = queueManager.previousSong
    val nextSong = queueManager.nextSong
    val isShuffled = queueManager.isShuffled
    val currentTrackNumber = queueManager.currentTrackNumber
    val playbackRepeatMode = queueManager.playbackRepeatMode

    val allPlaylists: StateFlow<List<Playlist>> = repository.getPlaylists()
        .map { entities -> entities.map { it.toPlaylist() } }
        .stateIn(
            scope = viewModelScope,
            // flow dies after stated duration, to prevent resource wastage
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )
    fun onPlaylistClick(playlistId: Long) {}

    // TODO clean up class after saving playlist
    private val _addTracksManager = MutableStateFlow<AddTracksManager?>(null)
    val addTracksManager: StateFlow<AddTracksManager?> = _addTracksManager.asStateFlow()
    private var createPlaylistJob: Job? = null
    fun onCreatePlaylistClick() {
        createPlaylistJob?.cancel()
        createPlaylistJob = viewModelScope.launch {
            val initialSongs = allSongs.first()
            _addTracksManager.value = AddTracksManager(initialSongs)
        }
    }

    private var finishPlaylistJob: Job? = null
    fun onFinishCreatePlaylist() {
        finishPlaylistJob?.cancel()
        finishPlaylistJob = viewModelScope.launch {
            _addTracksManager.value?.let { atm ->
                repository.createPlaylist(
                    // TODO feature, auto-generate playlist names
                    name = "mismatch",
                    songs = atm.addedSongs
                )
            }
        }
    }

    val allSongs: StateFlow<List<Song>> = repository.getSongs()
        .map { entities -> entities.map{ it.toSong() } }
        .stateIn(
            scope = viewModelScope,
            // flow dies after stated duration, to prevent resource wastage
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private val syncManager = SyncManager(viewModelScope, repository::sync)
    val syncState = syncManager.syncState
    fun sync() = syncManager.triggerSync()

    private val playerNotificationBroadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            when (p1?.action) {
                MusicForegroundService.Actions.ACTION_PAUSE_PLAY_SONG -> {
                    if (playerState.value.isPlaying) {
                        pauseSong()
                    } else {
                        playerState.value.loadedSong?.let { playSong(it) }
                    }
                }

                MusicForegroundService.Actions.ACTION_NEXT_SONG -> onNextSong(isUserPressNext = true)
                MusicForegroundService.Actions.ACTION_PREVIOUS_SONG -> onPreviousSong()
                MusicForegroundService.Actions.ACTION_SEEK_TO -> {
                    val pos = p1.getLongExtra(
                        MusicForegroundService.Extras.EXTRA_SEEK_POSITION,
                        0L
                    )
                    val durationMs = playerState.value.durationMs
                    val progress = pos.toFloat() / durationMs.toFloat()
                    songPlayer.seekTo(progress)
                }
            }
        }
    }


    init {
        viewModelScope.launch {
            songPlayer.onPlaybackComplete.collect {
                onNextSong(isUserPressNext = false)
            }
        }

        viewModelScope.launch {
            playerState
                .distinctUntilChangedBy {
                    Triple(it.loadedSong, it.isPlaying, it.currentPositionMs)
                }
                .collect { state ->
                    val song = state.loadedSong ?: return@collect

                    val intent = Intent(
                        getApplication(),
                        MusicForegroundService::class.java,
                    ).apply {
                        action = MusicForegroundService.Actions.ACTION_PLAYER_STATE_UPDATE

                        putExtra(MusicForegroundService.Extras.EXTRA_SONG_TITLE, song.title)
                        putExtra(MusicForegroundService.Extras.EXTRA_SONG_ARTIST, song.artistName)
                        putExtra(MusicForegroundService.Extras.EXTRA_AAFP, song.albumArtFilePath)
                        putExtra(MusicForegroundService.Extras.EXTRA_IS_SONG_PLAYING, state.isPlaying)
                        putExtra(MusicForegroundService.Extras.EXTRA_CURRENT_POSITION_MS, state.currentPositionMs.toLong())
                        putExtra(MusicForegroundService.Extras.EXTRA_DURATION_MS, state.durationMs.toLong())
                    }
                    ContextCompat.startForegroundService(getApplication(), intent)
            }
        }

        val intentFilter = IntentFilter().apply {
            addAction(MusicForegroundService.Actions.ACTION_PAUSE_PLAY_SONG)
            addAction(MusicForegroundService.Actions.ACTION_NEXT_SONG)
            addAction(MusicForegroundService.Actions.ACTION_PREVIOUS_SONG)
            addAction(MusicForegroundService.Actions.ACTION_SEEK_TO)
        }
        ContextCompat.registerReceiver(
            getApplication(),
            playerNotificationBroadcastReceiver,
            intentFilter,
            ContextCompat.RECEIVER_NOT_EXPORTED,
        )
    }

    fun onSongItemClick(
        indexClickedSong: Int,
        songList: List<Song>,
    ) {
        queueManager.setup(indexClickedSong, songList)

        val song = songList[indexClickedSong]
        playSong(song)
    }

    private var playSongJob: Job? = null
    fun playSong(song: Song) {
        playSongJob?.cancel()
        playSongJob = viewModelScope.launch {
            songPlayer.play(
                song = song,
            )
        }
    }

    fun pauseSong() {
        songPlayer.pause()
    }

    fun seekSongTo(progress: Float) {
        songPlayer.seekTo(progress)
    }

    fun onNextSong(isUserPressNext: Boolean) {
        val nextSong = queueManager.next(isUserPressNext)
        nextSong?.let {
            playSong(it)
        }
    }

    fun onPreviousSong() {
        val previousSong = queueManager.previous()
        previousSong?.let {
            playSong(it)
        }
    }

    fun onSwapSong(fromIndex: Int, toIndex: Int) {
        queueManager.swapSongs(fromIndex, toIndex)
    }

    fun toggleShuffle() {
        queueManager.toggleShuffle()
    }

    fun toggleRepeatMode() {
        queueManager.toggleRepeatMode()
    }

    val playbackActions = PlaybackActions(
        onPlay = ::playSong,
        onPause = ::pauseSong,
        onSeekTo = ::seekSongTo,
        onNext = { onNextSong(isUserPressNext = true) },
        onPrev = ::onPreviousSong,
        toggleShuffle = ::toggleShuffle,
        toggleRepeatMode = ::toggleRepeatMode,
        onSwapSong = ::onSwapSong,
        onSongItemClick = ::onSongItemClick,
    )

    override fun onCleared() {
        super.onCleared()
        songPlayer.release()
        getApplication<Application>().unregisterReceiver(playerNotificationBroadcastReceiver)
    }
}