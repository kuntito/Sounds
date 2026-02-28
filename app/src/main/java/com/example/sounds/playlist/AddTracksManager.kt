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
import com.example.sounds.utils.subsequenceMatch

/**
 * manages the logic for building a playlist from a pool of songs.
 *
 * the pool is one of two things, a list of available songs or the results
 * from the user's search query.
 *
 * [exposedPool] is what the UI sees. initially it's all available songs,
 * and if the user searches it becomes the search result.
 *
 * [addedTracksIds] notifies the UI each time a song is added.
 */
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

    private val _addedSongs = mutableListOf<Song>()
    val addedSongs: List<Song>
        get() = _addedSongs

    var hasSongs by mutableStateOf(false)
        private set

    private var availableSongs = allSongs.toMutableList()

    // i want a copy of availableSongs, toList() works but doesn't say that.
    private fun availableSongsSnapshot() = availableSongs.toList()
    private val _exposedPool = MutableStateFlow(
        availableSongsSnapshot()
    )
    val exposedPool: StateFlow<List<Song>> = _exposedPool.asStateFlow()

    fun addSong(song: Song) {
        if (!hasSongs) {
            hasSongs = true
        }
        availableSongs.remove(song)

        _addedSongs.add(song)
        _addedTracksIds.tryEmit(song.id)
    }

    fun onSearchSong(rawQuery: String){
        val query = rawQuery.trim()
        if (query.isEmpty()) {
            _exposedPool.value = availableSongsSnapshot()
        } else {
            val searchResults = availableSongs.filter { song ->
                subsequenceMatch(query, song.title) ||
                subsequenceMatch(query, song.artistName)
            }
            _exposedPool.value = searchResults
        }
    }
}