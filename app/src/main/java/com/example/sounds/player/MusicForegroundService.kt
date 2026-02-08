package com.example.sounds.player

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MusicForegroundService: Service() {
    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }
}