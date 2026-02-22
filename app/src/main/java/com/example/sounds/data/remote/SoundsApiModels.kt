package com.example.sounds.data.remote

data class SongWithUrl(
    val id: String,
    val title: String,
    val artist: String,
    val durationMillis: Int,
    val albumArtUrl: String,
    val songUrl: String,
)

data class GetSongsUrlResponse(
    val success: Boolean,
    val songsWithUrl: List<SongWithUrl>? = null,
    val debug: Map<String, String>? = null,
)