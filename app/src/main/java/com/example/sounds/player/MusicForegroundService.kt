package com.example.sounds.player

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Build
import android.os.IBinder
import android.support.v4.media.MediaMetadataCompat
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import androidx.core.app.NotificationCompat
import androidx.media.app.NotificationCompat.MediaStyle

// a foreground service is work the app is doing that you want to show in the notifications pane
// every notification has a channel, think, a container where notifications can pop up
// `notificationId`, uniquely identifies every notification within that channel

// so, what's the order

// `onCreate` is called when the object is created.

// this calls the method, that creates the notification channel.

//then initializes the mediaSession..

//then to actually use the class, you call `onStartCommand`
//however, this isn't called directly by you, Android calls this under the hood.

//you start the service, `MusicForegroundService`, via startForegroundService(intent)

//this builds the UI for the notification
//and starts the foreground service..

//START_STICKY ensures.. if Android kills or attempts to kill the service.
//it spawns right back up
class MusicForegroundService: Service() {
    private val channelId = "music_playback"
    private val channelName = "Music Playback"
    private val notificationId = 1

    private lateinit var mediaSessionCompat: MediaSessionCompat
    private val mediaSessionTag = "soundsMediaSession"

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_LOW,
            )

            getSystemService(
                NotificationManager::class.java
            )
                .createNotificationChannel(notificationChannel)
        }
    }

    private fun getActionIntent(action: String): PendingIntent {
        val intent = Intent(this, MusicForegroundService::class.java).apply {
            this.action = action
        }
        return PendingIntent.getService(
            this,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    private fun buildMusicPlayerNotification(
        isPlayingSong: Boolean,
        songTitle: String,
        songArtistName: String,
        albumArtFilePath: String,
    ): Notification {
        val playPauseIcon = if (isPlayingSong) {
            android.R.drawable.ic_media_pause
        } else {
            android.R.drawable.ic_media_play
        }

        return NotificationCompat.Builder(this, channelId)
            .setContentTitle(songTitle)
            .setContentText(songArtistName)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setStyle(
                MediaStyle()
                    .setMediaSession(mediaSessionCompat.sessionToken)
                    .setShowActionsInCompactView(0, 1, 2)
            )
            .setLargeIcon(BitmapFactory.decodeFile(albumArtFilePath))
            .addAction(
                android.R.drawable.ic_media_previous,
                "Previous",
                getActionIntent(Actions.ACTION_PREVIOUS_SONG),
            )
            .addAction(
                playPauseIcon,
                "Play/Pause",
                getActionIntent(Actions.ACTION_PAUSE_PLAY_SONG)
            )
            .addAction(
                android.R.drawable.ic_media_next,
                "Next",
                getActionIntent(Actions.ACTION_NEXT_SONG),
            )
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()

        mediaSessionCompat = MediaSessionCompat(this, mediaSessionTag).apply {
            isActive = true
            setCallback(object : MediaSessionCompat.Callback() {
                override fun onPlay() {
                    sendBroadcast(Intent(Actions.ACTION_PAUSE_PLAY_SONG))
                }
                override fun onPause() {
                    sendBroadcast(Intent(Actions.ACTION_PAUSE_PLAY_SONG))
                }

                override fun onSkipToNext() {
                    sendBroadcast(Intent(Actions.ACTION_NEXT_SONG))
                }

                override fun onSkipToPrevious() {
                    sendBroadcast(Intent(Actions.ACTION_PREVIOUS_SONG))
                }

                override fun onSeekTo(pos: Long) {
                    sendBroadcast(
                        Intent(Actions.ACTION_SEEK_TO)
                            .putExtra(Extras.EXTRA_SEEK_POSITION, pos)
                    )
                }
            })
        }
    }

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.ACTION_PLAYER_STATE_UPDATE -> { handlePlayerStateUpdate(intent) }
        }

        return START_STICKY
    }

    private fun handlePlayerStateUpdate(intent: Intent) {
        val songTitle = intent.getStringExtra(Extras.EXTRA_SONG_TITLE) ?: "Unknown"
        val songArtist = intent.getStringExtra(Extras.EXTRA_SONG_ARTIST) ?: "Unknown"
        val albumArtFilePath = intent.getStringExtra(Extras.EXTRA_AAFP) ?: "Unknown"
        val isPlayingSong = intent.getBooleanExtra(
            Extras.EXTRA_IS_SONG_PLAYING, false
        )
        val currentPositionMs = intent.getLongExtra(Extras.EXTRA_CURRENT_POSITION_MS, 0L)
        val durationMs = intent.getLongExtra(Extras.EXTRA_DURATION_MS, 0L)


        val musicPlayerNotification = buildMusicPlayerNotification(
            isPlayingSong,
            songTitle,
            songArtist,
            albumArtFilePath,
        )


        startForeground(
            notificationId,
            musicPlayerNotification,
        )

        val playbackState = if (isPlayingSong) {
            PlaybackStateCompat.STATE_PLAYING
        } else {
            PlaybackStateCompat.STATE_PAUSED
        }

        // this brings the notification in front of other music player notifications
        mediaSessionCompat.setPlaybackState(
            PlaybackStateCompat.Builder()
                .setState(
                    playbackState,
                    currentPositionMs,
                    if (isPlayingSong) 1f else 0f
                )
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY_PAUSE
                            or PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                            or PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS
                            or PlaybackStateCompat.ACTION_SEEK_TO
                )
                .build()
        )

        mediaSessionCompat.setMetadata(
            MediaMetadataCompat.Builder()
                .putLong(MediaMetadataCompat.METADATA_KEY_DURATION, durationMs)
                .build()
        )
    }

    object Actions {
        const val ACTION_PAUSE_PLAY_SONG = "ACTION_PLAY_PAUSE_SONG"
        const val ACTION_PREVIOUS_SONG = "ACTION_PREVIOUS_SONG"
        const val ACTION_NEXT_SONG = "ACTION_NEXT_SONG"
        const val ACTION_PLAYER_STATE_UPDATE = "ACTION_PLAYER_STATE_UPDATE"
        const val ACTION_SEEK_TO = "ACTION_SEEK_TO"
    }
    object Extras {
        const val EXTRA_SONG_TITLE = "SONG_TITLE"
        const val EXTRA_SONG_ARTIST = "SONG_ARTIST"
        const val EXTRA_AAFP = "AAFP"
        const val EXTRA_IS_SONG_PLAYING = "IS_PLAYING_SONG"
        const val EXTRA_CURRENT_POSITION_MS = "CURRENT_POSITION_MS"
        const val EXTRA_DURATION_MS = "DURATION_MS"
        const val EXTRA_SEEK_POSITION = "EXTRA_SEEK_POSITION"
    }
}