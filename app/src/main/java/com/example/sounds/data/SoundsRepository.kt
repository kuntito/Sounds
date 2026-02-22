package com.example.sounds.data

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.sounds.data.local.song.SongDao
import com.example.sounds.data.local.song.SongEntity
import com.example.sounds.data.remote.FileDownloader
import com.example.sounds.data.remote.SongWithUrl
import com.example.sounds.data.remote.SoundsApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.File

class SoundsRepository(
    private val songDao: SongDao,
    private val soundsApi: SoundsApiService,
    private val context: Context,
) {
    fun getSongs(): Flow<List<SongEntity>> = flow {
        val dbSongs = songDao.getAllSongs().first()

        if (dbSongs.isEmpty()) {
            try {
                val response = soundsApi.getAllSongsUrl()
                if (response.songsWithUrl != null) {
                    downloadAndInsertSongsToDb(
                        response.songsWithUrl
                    )
                }
            } catch (e: Exception) {
                Log.d("sounds-tag", "sum' wrong, ${e.message}")
            }
        }

        emitAll(songDao.getAllSongs())
    }


    private suspend fun downloadAndInsertSongsToDb(
        songsWithUrl: List<SongWithUrl>
    ) {
        val musicDir = context.getExternalFilesDir(Environment.DIRECTORY_MUSIC) ?: return
        val albumArtDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES) ?: return

        for (songObj in songsWithUrl) {
            val songExt = extractExtension(songObj.songUrl, "mp3")
            val albumArtExtension = extractExtension(songObj.albumArtUrl, "jpg")

            val destSongFile = File(
                musicDir,
                    "${songObj.id}.${songExt}"
                )
            val destAlbumArtFile = File(
                albumArtDir,
                "${songObj.id}.${albumArtExtension}"
            )

            if (
                destSongFile.exists() && destAlbumArtFile.exists()
            ) continue

            val isDownloadSong = FileDownloader.downloadFile(
                songObj.songUrl,
                destSongFile,
            )
            val isDownloadAlbumArt = FileDownloader.downloadFile(
                songObj.albumArtUrl,
                destAlbumArtFile,
            )

            if (isDownloadSong && isDownloadAlbumArt) {
                val songEntity = SongEntity(
                    id = songObj.id,
                    title = songObj.title,
                    artist = songObj.artist,
                    songFilePath = destSongFile.absolutePath,
                    albumArtFilePath = destAlbumArtFile.absolutePath,
                    durationMillis = songObj.durationMillis,
                )
                songDao.insert(songEntity)
            }
        }
    }

    suspend fun getLocalPath(songId: String): String? {
        return songDao.getSongFilePath(songId)
    }
}

/**
 * Extracts the file extension from a URL pointing to a file, stripping any query parameters.
 *
 * Example:
 * ```
 * extractExtension("https://example.com/track.mp3?token=abc", "mp3")
 * // returns "mp3"
 * ```
 *                                                                                                                                                                  * @param url The URL to extract the extension from
 * @param default The fallback extension if none is found
 * @return The file extension without the leading dot
 */
private fun extractExtension(url: String, default: String): String {
    return url
        .substringAfterLast('.')
        .substringBefore('?', default)
}