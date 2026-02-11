package com.example.sounds.player

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build

/**
 * manages audio focus for the app.
 *
 * on Android, there's a single audio focus shared across all apps.
 * only one app can hold it at a time. when your music is playing
 * and you open a youtube video, youtube requests audio focus,
 * stealing it from your app — causing your music to stop.
 *
 * this class wraps that mechanism. it requests focus before playback
 * and listens for when another app takes it away, so SongPlayer
 * can respond accordingly (e.g. pause).
 *
 * without this, multiple playbacks would occur at the same time.
 */
class AudioFocusManager(context: Context) {
    private val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    private var audioFocusRequest: AudioFocusRequest? = null
    private var onFocusChange: ((Int) -> Unit)? = null

    fun setOnAudioFocusChangeListener(listener: (Int) -> Unit) {
        onFocusChange = listener
    }

    fun requestFocus(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val request = AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN)
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .build()
                )
                .setOnAudioFocusChangeListener { onFocusChange?.invoke(it) }
                .build()

            audioFocusRequest = request
            audioManager.requestAudioFocus(request) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        } else {
            @Suppress("DEPRECATION")
            audioManager.requestAudioFocus(
                { onFocusChange?.invoke(it) },
                AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN
            ) == AudioManager.AUDIOFOCUS_REQUEST_GRANTED
        }
    }

    fun releaseFocus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioFocusRequest?.let { audioManager.abandonAudioFocusRequest(it) }
        } else {
            @Suppress("DEPRECATION")
            audioManager.abandonAudioFocus { onFocusChange?.invoke(it) }
        }
    }
}