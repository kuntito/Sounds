package com.example.sounds.data.repository

import android.content.Context
import com.example.sounds.data.local.song.SongDao
import com.example.sounds.data.local.song.SongEntity
import com.example.sounds.data.remote.SoundsApiDataSource
import com.example.sounds.data.repository.sync.runSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class SoundsRepository(
    private val songDao: SongDao,
    private val soundsDS: SoundsApiDataSource,
    private val context: Context,
) {
    fun getSongs(): Flow<List<SongEntity>> = flow {
        val dbSongs = songDao.getAllSongs().first()

        if (dbSongs.isEmpty()) {
            sync()
        }

        emitAll(songDao.getAllSongs())
    }

    suspend fun sync(): Boolean = runSync(songDao, soundsDS, context)
}