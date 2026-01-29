package com.example.sounds.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sounds.data.SoundsRepository

class SongViewModelFactory(
    private val repository: SoundsRepository,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SongViewModel(repository) as T
    }
}