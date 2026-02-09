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
import com.example.sounds.player.QueueManager
import com.example.sounds.player.SongPlayer
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
    private val songPlayer = SongPlayer(viewModelScope)
    val playerState = songPlayer.playerState
    private val queueManager = QueueManager(viewModelScope)
    val songQueue = queueManager.queueOfSongs

    val currentSong = queueManager.currentSong
    val previousSong = queueManager.previousSong
    val nextSong = queueManager.nextSong


    val songs: StateFlow<List<Song>> = repository.getSongs()
        .map { entities -> entities.map{ it.toSong() } }
        .stateIn(
            scope = viewModelScope,
            // flow dies after stated duration, to prevent resource wastage
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )


    private val playPauseReceiver = object: BroadcastReceiver() {
        override fun onReceive(p0: Context?, p1: Intent?) {
            if (playerState.value.isPlaying) {
                pauseSong()
            }
            else {
                playerState.value.loadedSong?.let { playSong(it) }
            }
        }
    }


    init {
        viewModelScope.launch {
            songPlayer.onPlaybackComplete.collect {
                onNextSong()
            }
        }

        viewModelScope.launch {
            playerState
                .distinctUntilChangedBy { Pair(it.loadedSong, it.isPlaying) }
                .collect { state ->
                    val song = state.loadedSong ?: return@collect

                    val intent = Intent(
                        getApplication(),
                        MusicForegroundService::class.java,
                    ).apply {
                        action = MusicForegroundService.ACTION_PLAYER_STATE_UPDATE

                        putExtra(MusicForegroundService.EXTRA_SONG_TITLE, song.title)
                        putExtra(MusicForegroundService.EXTRA_SONG_ARTIST, song.artistName)
                        putExtra(MusicForegroundService.EXTRA_AAFP, song.albumArtFilePath)
                        putExtra(MusicForegroundService.EXTRA_IS_SONG_PLAYING, state.isPlaying)
                    }
                    ContextCompat.startForegroundService(getApplication(), intent)
            }
        }

        val intentFilter = IntentFilter(MusicForegroundService.ACTION_PAUSE_PLAY_SONG)
        ContextCompat.registerReceiver(
            getApplication(),
            playPauseReceiver,
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
        viewModelScope.launch {
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

    fun onNextSong() {
        val nextSong = queueManager.next()
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

    override fun onCleared() {
        super.onCleared()
        songPlayer.release()
        getApplication<Application>().unregisterReceiver(playPauseReceiver)
    }
}