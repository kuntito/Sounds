package com.example.sounds.player

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.example.sounds.data.models.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class QueueManager(
    private val scope: CoroutineScope,
    private val prefDataStore: DataStore<Preferences>
) {
    private val _queueOfSongs = MutableStateFlow<List<Song>>(emptyList())
    val queueOfSongs: StateFlow<List<Song>> = _queueOfSongs.asStateFlow()

    private var preShuffleQueue: List<Song>? = null

    private val _isShuffleOn = MutableStateFlow(false)
    val isShuffled: StateFlow<Boolean> = _isShuffleOn.asStateFlow()

    private val _currentIndex = MutableStateFlow(0)
    val currentTrackNumber: StateFlow<Int> = _currentIndex
        .map { it + 1}
        .stateIn(scope, SharingStarted.Eagerly, 1)

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

    init {
        scope.launch {
            val prefs = prefDataStore.data.first()
            _isShuffleOn.value = prefs[IS_SHUFFLE_ON_PREF_KEY] ?: false
        }
    }

    fun setup(
        indexClickedSong: Int,
        songList: List<Song>,
    ) {
        _queueOfSongs.value = songList
        _currentIndex.value = indexClickedSong

        if (_isShuffleOn.value) {
            shuffle()
        } else {
            preShuffleQueue = null
        }
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

    fun toggleShuffle() {
        if (_isShuffleOn.value) unshuffle() else shuffle()
    }

    private fun shuffle() {
        preShuffleQueue = queueOfSongs.value

        val currentSong = queueOfSongs.value[_currentIndex.value]
        val everyOtherSong = queueOfSongs.value
            .filterNot { it.id == currentSong.id }
            .shuffled()

        _queueOfSongs.value = listOf(currentSong) + everyOtherSong
        _currentIndex.value = 0

        persistShuffleState(true)
    }

    private fun unshuffle() {
        val currentSong = queueOfSongs.value[_currentIndex.value]
        // in case, songs were deleted in the shuffled state
        val stillExistingSongIds = queueOfSongs.value.map { it.id }.toSet()

        _queueOfSongs.value = (preShuffleQueue ?: return)
            .filter { it.id in stillExistingSongIds }
        _currentIndex.value = _queueOfSongs.value.indexOfFirst { it.id == currentSong.id }

        preShuffleQueue = null

        persistShuffleState(false)
    }

    private fun persistShuffleState(isOn: Boolean) {
        scope.launch {
            _isShuffleOn.value = isOn
            prefDataStore.edit {
                it[IS_SHUFFLE_ON_PREF_KEY] = isOn
            }
        }
    }

    companion object {
        private val IS_SHUFFLE_ON_PREF_KEY = booleanPreferencesKey("is_shuffle_on")
    }
}