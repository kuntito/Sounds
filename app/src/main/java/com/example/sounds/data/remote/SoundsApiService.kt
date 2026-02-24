package com.example.sounds.data.remote

import retrofit2.http.GET

interface SoundsApiService {
    @GET("api/sounds/all-songs-with-url")
    suspend fun getAllSongsUrl(): GetSongsUrlResponse
}

class SoundsApiDataSource(
    private val api: SoundsApiService
) {
    suspend fun safeGetAllSongsUrl() = safeApiCall(
        ApiCallInfo(
            "`getAllSongsUrl` fetches all songs along with their download URLs",
            fn = {
                api.getAllSongsUrl()
            }
        )
    )
}