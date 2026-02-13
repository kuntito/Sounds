package com.example.sounds.ui

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sounds.data.SoundsRepository
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.toSong
import com.example.sounds.player.MusicForegroundService
import com.example.sounds.player.PlaybackActions
import com.example.sounds.player.QueueManager
import com.example.sounds.player.SongPlayer
import com.example.sounds.prefDataStore
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(
    application: Application,
    private val repository: SoundsRepository,
): AndroidViewModel(application) {

    private var playSongJob: Job? = null
    private val songPlayer = SongPlayer(viewModelScope, application)
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

    val songs: StateFlow<List<Song>> = repository.getSongs()
        .map { entities -> entities.map{ it.toSong() } }
        .stateIn(
            scope = viewModelScope,
            // flow dies after stated duration, to prevent resource wastage
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

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

    fun playSong(song: Song) {
        playSongJob?.cancel()
        playSongJob = viewModelScope.launch {
            val path = repository.getLocalPath(song.id) ?: return@launch

            songPlayer.play(
                song = song,
                filePath = path,
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