package com.example.sounds.data.repository.sync

import android.content.Context
import com.example.sounds.data.local.song.SongDao
import com.example.sounds.data.remote.SoundsApiDataSource

suspend fun runSync(
    songDao: SongDao,
    soundsDS: SoundsApiDataSource,
    context: Context,
): Boolean {
    val syncCategories = getSyncCategories(
        songDao = songDao,
        soundsDS = soundsDS,
    ) ?: return false

//    intentionally commented out
//    handleExistingSongs(syncCategories.existingSongs)
    handleNewSongs(syncCategories.newSongs, context, songDao)
    handleMissingSongs(syncCategories.missingSongs, songDao)

    return true
}