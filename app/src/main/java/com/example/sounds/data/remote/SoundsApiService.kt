package com.example.sounds.data.remote

import retrofit2.http.GET

interface SoundsApiService {
    @GET("api/sounds/all-songs-with-url")
    suspend fun getAllSongsUrl(): GetSongsUrlResponse
}