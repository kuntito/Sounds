package com.example.sounds.data.remote

import retrofit2.http.GET

data class SongUrl(
    val songId: String,
    val url: String,
)

data class GetSongsUrlResponse(
    val success: Boolean,
    val songUrls: List<SongUrl>? = null,
    val debug: Map<String, String>? = null,
)

interface SoundsApi {
    @GET("api/sounds/all-songs-url")
    suspend fun getAllSongsUrl(): GetSongsUrlResponse
}