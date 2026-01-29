package com.example.sounds.data

import android.content.Context
import android.os.Environment
import com.example.sounds.data.local.SongDao
import com.example.sounds.data.local.SongEntity
import com.example.sounds.data.remote.SongDownloader
import com.example.sounds.data.remote.SongUrl
import com.example.sounds.data.remote.SoundsApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.File

class SoundsRepository(
    private val songDao: SongDao,
    private val soundsApi: SoundsApi,
    private val context: Context,
) {
    fun getSongs(): Flow<List<SongEntity>> = flow {
        val dbSongs = songDao.getAllSongs().first()

        if (dbSongs.isEmpty()) {
            val response = soundsApi.getAllSongsUrl()
            if (response.songUrls != null) {
                downloadAndInsertSongsToDb(
                    response.songUrls
                )
            }
        }

        emitAll(songDao.getAllSongs())
    }


    private suspend fun downloadAndInsertSongsToDb(
        songUrls: List<SongUrl>
    ) {
        val musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) ?: return

        for (songUrl in songUrls) {
            val destFile = File(
                musicDir,
                    "${songUrl.songId}.mp3"
                )

            if (destFile.exists()) continue

            val downloadSuccess = SongDownloader.downloadSong(
                songUrl.url,
                destFile,
            )

            if (downloadSuccess) {
                val songEntity = SongEntity(
                    id = songUrl.songId,
                    title = "sumn' wrong..",
                    artist = "sumn' wrong...",
                    localPath = destFile.absolutePath,
                )
                songDao.insert(songEntity)
            }

        }
    }
}