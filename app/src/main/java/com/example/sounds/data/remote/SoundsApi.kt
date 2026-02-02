package com.example.sounds.data.remote

import retrofit2.http.GET

data class SongWithUrl(
    val id: String,
    val title: String,
    val artist: String,
    val albumArtUrl: String,
    val songUrl: String,
)

data class GetSongsUrlResponse(
    val success: Boolean,
    val songsWithUrl: List<SongWithUrl>? = null,
    val debug: Map<String, String>? = null,
)

interface SoundsApi {
    @GET("api/sounds/all-songs-with-url")
    suspend fun getAllSongsUrl(): GetSongsUrlResponse
}