package com.example.sounds.data.models

import com.example.sounds.data.local.playlist.PlaylistEntity
import com.example.sounds.data.local.playlist.PlaylistWithSongsEntity

data class Playlist (
    val id: Long,
    val playlistName: String,
)

fun PlaylistEntity.toPlaylist() = Playlist(
    id = id,
    playlistName = name
)

data class PlaylistWithSongs(
    val playlist: Playlist,
    val playlistSongs: List<Song>,
)

fun PlaylistWithSongsEntity.toPlaylistWithSongs() = PlaylistWithSongs(
    playlist = playlist.toPlaylist(),
    playlistSongs = playlistSongs.map { it.toSong() },
)

val dummyPlaylist = Playlist(
    id = 0L,
    playlistName = "flames",
)

val dummyPlaylistList = listOf(
    Playlist(
        id = 0L,
        playlistName = "flames",
    ),
        Playlist(
        id = 0L,
        playlistName = "gym",
    ),
        Playlist(
        id = 0L,
        playlistName = "eve",
    ),
        Playlist(
        id = 0L,
        playlistName = "the third",
    ),
        Playlist(
        id = 0L,
        playlistName = "charades",
    ),

)