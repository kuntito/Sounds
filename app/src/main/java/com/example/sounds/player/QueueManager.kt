package com.example.sounds.player

import com.example.sounds.data.models.Song
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class QueueManager {
    private val _queueOfSongs = MutableStateFlow<List<Song>>(emptyList())
    val queueOfSongs: StateFlow<List<Song>> = _queueOfSongs.asStateFlow()
    private var currentIndex: Int = 0

    fun setup(
        indexClickedSong: Int,
        songList: List<Song>,
    ) {
        _queueOfSongs.value = songList
        currentIndex = indexClickedSong
    }

    fun next(): Song? {
        if (currentIndex + 1 == queueOfSongs.value.size) return null

        currentIndex++
        return queueOfSongs.value[currentIndex]
    }

    fun previous(): Song? {
        if (currentIndex == 0) return null

        currentIndex--
        return queueOfSongs.value[currentIndex]
    }

    fun swapSongs(fromIndex: Int, toIndex: Int) {
        _queueOfSongs.value = _queueOfSongs.value.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }

        updateCurrentIndexAfterSwap(fromIndex, toIndex)
    }

    fun updateCurrentIndexAfterSwap(fromIndex: Int, toIndex: Int) {
        val isDragFromBeforeToAfterCurrentSong = fromIndex < currentIndex && toIndex >= currentIndex
        val isDragFromAfterToBeforeCurrentSong = fromIndex > currentIndex && toIndex <= currentIndex
        if (fromIndex == currentIndex) {
            currentIndex = toIndex
        } else if (isDragFromBeforeToAfterCurrentSong) {
            currentIndex--
        } else if (isDragFromAfterToBeforeCurrentSong) {
            currentIndex++
        }
    }
}