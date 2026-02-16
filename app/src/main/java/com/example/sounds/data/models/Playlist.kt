package com.example.sounds.data.models

import com.example.sounds.data.local.playlist.PlaylistEntity

data class Playlist (
    val id: Long,
    val playlistName: String,
)

fun PlaylistEntity.toPlaylist() = Playlist(
    id = id,
    playlistName = name
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