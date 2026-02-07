package com.example.sounds.player

import com.example.sounds.data.models.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class QueueManager(
    scope: CoroutineScope
) {
    private val _queueOfSongs = MutableStateFlow<List<Song>>(emptyList())
    val queueOfSongs: StateFlow<List<Song>> = _queueOfSongs.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)

    val currentSong: StateFlow<Song?> = combine(
        queueOfSongs,
        _currentIndex
    ) { queue, index ->
        queue.getOrNull(index)
    }.stateIn(scope, SharingStarted.Eagerly, null)

    val previousSong: StateFlow<Song?> = combine(
        queueOfSongs,
        _currentIndex
    ) { queue, index ->
        queue.getOrNull(index - 1)
    }.stateIn(scope, SharingStarted.Eagerly, null)

    val nextSong: StateFlow<Song?> = combine(
        queueOfSongs,
        _currentIndex
    ) { queue, index ->
        queue.getOrNull(index + 1)
    }.stateIn(scope, SharingStarted.Eagerly, null)


    fun setup(
        indexClickedSong: Int,
        songList: List<Song>,
    ) {
        _queueOfSongs.value = songList
        _currentIndex.value = indexClickedSong
    }

    fun next(): Song? {
        if (_currentIndex.value + 1 == queueOfSongs.value.size) return null

        _currentIndex.value++
        return queueOfSongs.value[_currentIndex.value]
    }

    fun previous(): Song? {
        if (_currentIndex.value == 0) return null

        _currentIndex.value--
        return queueOfSongs.value[_currentIndex.value]
    }

    fun swapSongs(fromIndex: Int, toIndex: Int) {
        _queueOfSongs.value = _queueOfSongs.value.toMutableList().apply {
            add(toIndex, removeAt(fromIndex))
        }

        updateCurrentIndexAfterSwap(fromIndex, toIndex)
    }

    fun updateCurrentIndexAfterSwap(fromIndex: Int, toIndex: Int) {
        val isDragFromBeforeToAfterCurrentSong = fromIndex < _currentIndex.value && toIndex >= _currentIndex.value
        val isDragFromAfterToBeforeCurrentSong = fromIndex > _currentIndex.value && toIndex <= _currentIndex.value
        if (fromIndex == _currentIndex.value) {
            _currentIndex.value = toIndex
        } else if (isDragFromBeforeToAfterCurrentSong) {
            _currentIndex.value--
        } else if (isDragFromAfterToBeforeCurrentSong) {
            _currentIndex.value++
        }
    }
}