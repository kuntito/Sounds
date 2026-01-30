package com.example.sounds.player

import android.media.MediaPlayer
import com.example.sounds.data.models.Song
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class PlayerState(
    val currentSong: Song? = null,
    val isPlaying: Boolean = false,
    val currentPositionMs: Int = 0,
    val durationMs: Int = 0,
) {
    val playProgress: Float
        get() = if (durationMs > 0) {
            currentPositionMs.toFloat() / durationMs.toFloat()
        } else {
            0f
        }
}

class SongPlayer(private val scope: CoroutineScope) {
    private var mediaPlayer: MediaPlayer? = null

    private val _playerState = MutableStateFlow(PlayerState())
    val playerState: StateFlow<PlayerState> = _playerState.asStateFlow()

    private var positionUpdateJob: Job? = null

    fun play(song: Song, filePath: String) {
        scope.launch(Dispatchers.IO) {
            try {

                val isNewSong = _playerState.value.let {
                    it.currentSong?.id != song.id
                }

                if (isNewSong) {
                    startPlayback(filePath)
                } else {
                    mediaPlayer?.start()
                }

                _playerState.value = _playerState.value.copy(
                    currentSong = song,
                    isPlaying = true,
                    durationMs = mediaPlayer?.duration ?: 0
                )

                startPositionUpdates()

            } catch (e: Exception) {
                mediaPlayer?.reset()
            }
        }
    }


    fun startPositionUpdates() {
        positionUpdateJob?.cancel()
        positionUpdateJob = scope.launch {
            try {
                while (mediaPlayer?.isPlaying == true) {
                    _playerState.value = _playerState.value.copy(
                        currentPositionMs = mediaPlayer?.currentPosition ?: 0
                    )
                    delay(1000)
                }
            } catch(e: Exception) {

            }
        }
    }

    fun seekTo(progress: Float) {
        val durationMs = _playerState.value.durationMs
        if (durationMs > 0) {
            val newPositionMs = (progress * durationMs).toInt()
            mediaPlayer?.seekTo(newPositionMs)
            _playerState.value = _playerState.value.copy(
                currentPositionMs = newPositionMs
            )
        }
    }

    private fun initializePlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer()
        }
    }

    private fun startPlayback(filePath: String) {
        initializePlayer()

        mediaPlayer?.apply {
            reset()
            setDataSource(filePath)
            setOnCompletionListener {
                positionUpdateJob?.cancel()
            }
            prepare()
            start()
        }
    }

    fun pause() {
        mediaPlayer?.pause()
        _playerState.value = _playerState.value.copy(
            isPlaying = false,
        )
        positionUpdateJob?.cancel()
    }


    fun release() {
        mediaPlayer?.release()
        _playerState.value = PlayerState()
        positionUpdateJob?.cancel()
    }
}