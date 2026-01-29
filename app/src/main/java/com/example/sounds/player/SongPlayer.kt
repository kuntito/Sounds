package com.example.sounds.player

import android.media.MediaPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SongPlayer(private val scope: CoroutineScope) {
    private var mediaPlayer: MediaPlayer? = null

    private val _currentSongId = MutableStateFlow<String?>(null)
    val currentSongId: StateFlow<String?> = _currentSongId.asStateFlow()

    private val _playbackPosition = MutableStateFlow(0)
    val playbackPosition: StateFlow<Int> = _playbackPosition.asStateFlow()

    private var positionUpdateJob: Job? = null

    fun play(songId: String, filePath: String) {
        scope.launch(Dispatchers.IO) {
            try {

                if (mediaPlayer == null) {
                    mediaPlayer = MediaPlayer()
                }

                mediaPlayer?.apply {
                    reset()
                    setDataSource(filePath)
                    setOnCompletionListener {
                        _currentSongId.value = null
                        positionUpdateJob?.cancel()
                    }
                    prepare()
                    start()
                }

                startPositionUpdates()
                _currentSongId.value = songId

            } catch (e: Exception) {
                mediaPlayer?.reset()
            }
        }
    }

    private fun startPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = scope.launch {
            while (mediaPlayer?.isPlaying == true) {
                _playbackPosition.value = mediaPlayer?.currentPosition ?: 0
                delay(1000)
            }
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        positionUpdateJob?.cancel()
    }

    fun resume() {
        mediaPlayer?.start()
        startPositionUpdates()
    }

//    fun seekTo(positionTo: Int) {
//
//    }

    fun release() {
        positionUpdateJob?.cancel()
        mediaPlayer?.release()
    }
}