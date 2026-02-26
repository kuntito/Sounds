package com.example.sounds.playlist

import androidx.compose.runtime.mutableStateOf
import com.example.sounds.data.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue


// TODO needs a docstring...
class AddTracksManager(allSongs: List<Song>) {

    private val _addedTracksIds = MutableSharedFlow<String>(
        // a MutableSharedFlow has a buffer, a queue of emissions waiting
        // by default it's 0, meaning if no collector is ready, the emission is
        // immediately dropped.

        // setting buffer capacity to `1` means hold one emission in the queue.
        // so the emission waits until it's picked up.
        extraBufferCapacity = 1
    )
    val addedTracksIds: Flow<String> = _addedTracksIds.asSharedFlow()

    private val addedSongs = mutableListOf<Song>()
    var hasSongs by mutableStateOf(false)
        private set

    private var songPool = allSongs
    private val _exposedPool = MutableStateFlow(songPool)
    val exposedPool: StateFlow<List<Song>> = _exposedPool.asStateFlow()

    fun addSong(song: Song) {
        if (!hasSongs) {
            hasSongs = true
        }
        addedSongs.add(song)
        _addedTracksIds.tryEmit(song.id)
        // TODO you want to remove every added song from `songPool`
        //  so the search operation only shows unadded songs
    }
}