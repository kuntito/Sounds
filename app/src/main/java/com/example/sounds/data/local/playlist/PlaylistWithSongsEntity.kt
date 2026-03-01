package com.example.sounds.data.local.playlist

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.example.sounds.data.local.song.SongEntity

/**
 * a playlist and its songs.
 *
 * there are three entities involved:
 * - PlaylistEntity — the playlist data
 * - PlaylistSongEntity — maps each playlist id to a song id
 * - SongEntity — the song data
 *
 * this class returns a playlist along with all its songs:
 * PlaylistWithSongs(
 *     playlist = PlaylistEntity(id=1, name="my playlist"),
 *     songs = [
 *         SongEntity(id="abc", title="song 1"),
 *         SongEntity(id="def", title="song 2"),
 *     ]
 * )
 *
 * PlaylistEntity links to PlaylistSongEntity via:
 *     @Relation(
 *         parentColumn = "id",
 *         entityColumn = ...,
 *         associateBy = Junction(
 *             value = PlaylistSongEntity::class,
 *             parentColumn = "playlistId",
 *             entityColumn = ...,
 *         )
 *     )
 *
 * PlaylistSongEntity links to SongEntity via:
 *     @Relation(
 *         parentColumn = ...,
 *         entityColumn = "id",
 *         associateBy = Junction(
 *             value = PlaylistSongEntity::class,
 *             parentColumn = ...,
 *             entityColumn = "songId",
 *         )
 *     )
 *
 *  SQL equivalent:
 *      SELECT * FROM playlists
 *      JOIN playlist_songs ON playlists.id = playlist_songs.playlistId
 *      JOIN songs ON playlist_songs.songId = songs.id
 *      WHERE playlists.id = :playlistId
 *
 *  Room doesn't support many-to-many directly, so Junction is used to
 *  bridge two one-to-many relationships through PlaylistSongEntity.
 */
data class PlaylistWithSongsEntity(
    @Embedded val playlist: PlaylistEntity,
    @Relation(
        parentColumn = "id", // PlaylistEntity
        entityColumn = "id", // PlaylistSongEntity
        associateBy = Junction(
            value = PlaylistSongEntity::class,
            parentColumn = "playlistId",
            entityColumn = "songId",
        )
    )
    val playlistSongs: List<SongEntity>
)