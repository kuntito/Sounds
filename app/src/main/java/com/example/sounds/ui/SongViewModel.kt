package com.example.sounds.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sounds.data.SoundsRepository
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.toSong
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class SongViewModel(
    private val repository: SoundsRepository
): ViewModel() {
    val songs: StateFlow<List<Song>> = repository.getSongs()
        .map { entities -> entities.map{ it.toSong() } }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000), // TODO what's this?
            initialValue = emptyList(),
        )

}