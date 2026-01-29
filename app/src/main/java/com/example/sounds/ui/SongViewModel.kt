package com.example.sounds.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sounds.data.SoundsRepository
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.toSong
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
    val currentSongId: StateFlow<String?> = songPlayer.currentSongId

    val songs: StateFlow<List<Song>> = repository.getSongs()
        .map { entities -> entities.map{ it.toSong() } }
        .stateIn(
            scope = viewModelScope,
            // flow dies after stated duration, to prevent resource wastage
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    fun playSong(song: Song) {
        viewModelScope.launch {
            val path = repository.getLocalPath(song.id) ?: return@launch
            songPlayer.play(
                songId = song.id,
                filePath = path,
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        songPlayer.release()
    }
}