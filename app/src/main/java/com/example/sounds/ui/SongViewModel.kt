package com.example.sounds.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sounds.data.SoundsRepository
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.toSong
import com.example.sounds.player.QueueManager
import com.example.sounds.player.SongPlayer
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SongViewModel(
    private val repository: SoundsRepository,
): ViewModel() {
    private val songPlayer = SongPlayer(viewModelScope)
    private val queueManager = QueueManager()
    val playerState = songPlayer.playerState

    val songs: StateFlow<List<Song>> = repository.getSongs()
        .map { entities -> entities.map{ it.toSong() } }
        .stateIn(
            scope = viewModelScope,
            // flow dies after stated duration, to prevent resource wastage
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

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

    override fun onCleared() {
        super.onCleared()
        songPlayer.release()
    }
}