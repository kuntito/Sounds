package com.example.sounds.player

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
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

enum class PlaybackRepeatModes{
    NoRepeat,
    RepeatOne,
    RepeatAll,
}

class QueueManager(
    private val scope: CoroutineScope,
    private val prefDataStore: DataStore<Preferences>
) {
    private val _queueOfSongs = MutableStateFlow<List<Song>>(emptyList())
    val queueOfSongs: StateFlow<List<Song>> = _queueOfSongs.asStateFlow()

    private var preShuffleQueue: List<Song>? = null

    private val _isShuffleOn = MutableStateFlow(false)
    val isShuffled: StateFlow<Boolean> = _isShuffleOn.asStateFlow()

    private val _playbackRepeatMode = MutableStateFlow(PlaybackRepeatModes.NoRepeat)
    val playbackRepeatMode: StateFlow<PlaybackRepeatModes> = _playbackRepeatMode.asStateFlow()
    private val isRepeatOne
        get() = _playbackRepeatMode.value == PlaybackRepeatModes.RepeatOne
    private val isRepeatAll
        get() = _playbackRepeatMode.value == PlaybackRepeatModes.RepeatAll


    private val _currentIndex = MutableStateFlow(0)
    private val currentIndex
        get() = _currentIndex.value
    private val isLastSong
        get() = currentIndex == _queueOfSongs.value.lastIndex

    val currentTrackNumber: StateFlow<Int> = _currentIndex
        .map { it + 1 }
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

            val repeatModeName = prefs[REPEAT_MODE_PREF_KEY] ?: PlaybackRepeatModes.NoRepeat.name
            _playbackRepeatMode.value = PlaybackRepeatModes.valueOf(repeatModeName)
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

    fun next(isUserPressNext: Boolean): Song? {
        val nextSongIndex = if (isUserPressNext) {
            getNextSongIndexOnUserPressNext()
        } else {
            getNextSongIndexOnSongEnd()
        }

        if (nextSongIndex == null) return null

        _currentIndex.value = nextSongIndex

        return queueOfSongs.value[_currentIndex.value]
    }

    private fun getNextSongIndexOnSongEnd(): Int? {
        return if (isRepeatOne) {
            currentIndex
        } else if (isRepeatAll && isLastSong) {
            0
        } else if (isLastSong) {
            null
        } else {
            currentIndex + 1
        }
    }

    private fun getNextSongIndexOnUserPressNext(): Int? {
        return if (isLastSong && isRepeatAll) {
            0
        } else if (isLastSong) {
            null
        } else {
            currentIndex + 1
        }
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

    private fun updateCurrentIndexAfterSwap(fromIndex: Int, toIndex: Int) {
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

    fun toggleRepeatMode() {
        _playbackRepeatMode.value = when (_playbackRepeatMode.value) {
            PlaybackRepeatModes.NoRepeat -> PlaybackRepeatModes.RepeatOne
            PlaybackRepeatModes.RepeatOne -> PlaybackRepeatModes.RepeatAll
            PlaybackRepeatModes.RepeatAll -> PlaybackRepeatModes.NoRepeat
        }
        persistPlaybackRepeatMode(_playbackRepeatMode.value)
    }

    private fun persistPlaybackRepeatMode(mode: PlaybackRepeatModes) {
        scope.launch {
            prefDataStore.edit {
                it[REPEAT_MODE_PREF_KEY] = mode.name
            }
        }
    }

    companion object {
        private val IS_SHUFFLE_ON_PREF_KEY = booleanPreferencesKey("is_shuffle_on")
        private val REPEAT_MODE_PREF_KEY = stringPreferencesKey("repeat_mode")
    }
}