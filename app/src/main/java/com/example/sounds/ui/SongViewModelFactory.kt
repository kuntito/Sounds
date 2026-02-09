package com.example.sounds.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sounds.data.SoundsRepository

class SongViewModelFactory(
    private val application: Application,
    private val repository: SoundsRepository,
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SongViewModel(
            application,
            repository
        ) as T
    }
}