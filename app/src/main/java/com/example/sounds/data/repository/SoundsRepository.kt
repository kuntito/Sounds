package com.example.sounds.data.repository

import android.content.Context
import androidx.room.withTransaction
import com.example.sounds.data.local.SoundsDb
import com.example.sounds.data.local.playlist.PlaylistDao
import com.example.sounds.data.local.playlist.PlaylistEntity
import com.example.sounds.data.local.playlist.PlaylistSongEntity
import com.example.sounds.data.local.playlist.PlaylistWithSongsEntity
import com.example.sounds.data.local.song.SongDao
import com.example.sounds.data.local.song.SongEntity
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.toSong
import com.example.sounds.data.remote.SoundsApiDataSource
import com.example.sounds.data.repository.sync.runSync
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow

class SoundsRepository(
    private val songDao: SongDao,
    private val playlistDao: PlaylistDao,
    private val db: SoundsDb,
    private val soundsDS: SoundsApiDataSource,
    private val context: Context,
) {
    // TODO i should be returning domain models not entities, do the mapping in here.
    fun getSongs(): Flow<List<SongEntity>> = flow {
        val dbSongs = songDao.getAllSongs().first()

        if (dbSongs.isEmpty()) {
            sync()
        }

        emitAll(songDao.getAllSongs())
    }

    suspend fun getSongsNotInPlaylist(playlistId: Long): List<Song>
        = songDao.getSongsNotInPlaylist(playlistId).map { it.toSong() }

    suspend fun sync(): Boolean = runSync(songDao, soundsDS, context)

    fun getPlaylist(playlistId: Long): Flow<PlaylistEntity>
        = playlistDao.getPlaylist(playlistId)

    fun getAllPlaylists(): Flow<List<PlaylistEntity>>
        = playlistDao.getAllPlaylists()

    fun getPlaylistWithSongs(playlistId: Long): Flow<PlaylistWithSongsEntity?>
        = playlistDao.getPlaylistWithSongs(playlistId)

    suspend fun updatePlaylist(
        playlistId: Long,
        addedSongs: List<Song>,
    ) {
        val playlistSongEntities = addedSongs.map { song ->
            PlaylistSongEntity(
                playlistId = playlistId,
                songId = song.id
            )
        }
        playlistDao.addManySongsToPlaylist(playlistSongEntities)
    }

    suspend fun removeSongFromPlaylist(songId: String, playlistId: Long): Boolean {
        return playlistDao.removeSongFromPlaylist(songId, playlistId) > 0
    }

    suspend fun createPlaylist(name: String, songs: List<Song>) {
        db.withTransaction {
            val playlistId = playlistDao.createPlaylist(PlaylistEntity(
                name = name
            ))

            val playlistSongs = songs.map { song ->
                PlaylistSongEntity(
                    playlistId = playlistId,
                    songId = song.id
                )
            }

            playlistDao.addManySongsToPlaylist(playlistSongs)
        }
    }

    suspend fun deletePlaylist(playlistId: Long) {
        playlistDao.deletePlaylist(playlistId)
    }
}