package com.example.sounds.data.repository.sync

import android.content.Context
import android.os.Environment
import android.util.Log
import com.example.sounds.data.local.song.SongDao
import com.example.sounds.data.local.song.SongEntity
import com.example.sounds.data.remote.FileDownloader
import com.example.sounds.data.remote.SongWithUrl
import com.example.sounds.data.remote.SoundsApiDataSource
import com.example.sounds.data.repository.extractExtension
import com.example.sounds.soundsDebugTag
import kotlinx.coroutines.flow.first
import java.io.File

/**
 * the sync operation ensures the songs on the device are the same as the ones on the remote.
 * for that, it needs to know the new songs, the existing songs, and the missing songs.
 *
 * - **new**: songs on the remote, not yet on the device.
 * - **existing**: songs that exist both locally and remotely. these usually don't change,
 *   unless their metadata was updated.
 * - **missing**: songs currently on the device but no longer on the remote, probably deleted.
 *
 * @return a [SyncCategories] object, or null if the remote request failed.
 */
suspend fun getSyncCategories(
    songDao: SongDao,
    soundsDS: SoundsApiDataSource,
): SyncCategories? {
    val idsLocalSongs = songDao
        .getAllSongs()
        .first() // grabs a snapshot of songs, further mods to db won't update this variable
        .map {
            it.id
        }
        .toMutableSet()

    val newSongs = mutableListOf<SongWithUrl>()
    val existingSongs = mutableListOf<SongWithUrl>()

    val response = soundsDS.safeGetAllSongsUrl() ?: return null
    val allSongs = response.songsWithUrl ?: return null

    allSongs.forEach { song ->
        if (song.id in idsLocalSongs) {
            existingSongs.add(song)
            idsLocalSongs.remove(song.id)
        } else {
            newSongs.add(song)
        }
    }

    // at this point, only ids left are local songs not on remote
    val missingSongs = idsLocalSongs

    return SyncCategories(
        existingSongs = existingSongs,
        newSongs = newSongs,
        missingSongs = missingSongs,
    )
}


/**
 * method does nothing.
 *
 * TODO: not handling this for now.
 *
 * the plan was to handle metadata changes for existing songs,
 * but it's not clear what md would have changed, going ahead would mean
 * updating every metadata for every existing song.
 *
 * the workaround is to treat metadata changes as
 * new songs — same file behind the scenes, different ID on the API side.
 */
fun handleExistingSongs(existingSongs: List<SongWithUrl>) { }

suspend fun handleNewSongs(
    newSongs: List<SongWithUrl>,
    context: Context,
    songDao: SongDao,
) {
    downloadAndInsertSongsToDb(
        songsWithUrl = newSongs,
        context = context,
        songDao = songDao,
    )
    Log.d(soundsDebugTag, "sync found ${newSongs.size} new songs")
}

/**
 * downloads the song file and album art for each song, then inserts them into the local database.
 * if a song's files already exist on the device, it's skipped.
 * a song is only inserted into the database if both files downloaded successfully.
 */
private suspend fun downloadAndInsertSongsToDb(
    songsWithUrl: List<SongWithUrl>,
    context: Context,
    songDao: SongDao,
) {
    val musicDir = context.getExternalFilesDir(
        Environment.DIRECTORY_MUSIC
    ) ?: return
    val albumArtDir = context.getExternalFilesDir(
        Environment.DIRECTORY_PICTURES
    ) ?: return

    for (songObj in songsWithUrl) {
        val songExt = extractExtension(songObj.songUrl, "mp3")
        val albumArtExtension = extractExtension(songObj.albumArtUrl, "jpg")

        val songFileName = "${songObj.id}.${songExt}"
        val albumArtFileName = "${songObj.id}.${albumArtExtension}"

        val destSongFile = File(
            musicDir,
            songFileName,
        )
        val destAlbumArtFile = File(
            albumArtDir,
            albumArtFileName,
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


/**
 * handles songs that are no longer on the remote.
 * for each missing song, it removes the record from the database first,
 * then deletes the song file and album art from the device.
 */
suspend fun handleMissingSongs(
    missingSongs: Set<String>,
    songDao: SongDao,
) {
    for (songId in missingSongs) {
        val song = songDao.getSongById(songId) ?: continue

        songDao.delete(songId)

        File(song.songFilePath).delete()
        File(song.albumArtFilePath).delete()
    }
    Log.d(soundsDebugTag, "sync revealed ${missingSongs.size} missing songs")
}