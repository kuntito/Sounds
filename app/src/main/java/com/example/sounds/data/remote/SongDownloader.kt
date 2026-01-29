package com.example.sounds.data.remote

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream

object SongDownloader {
    private val httpClient = OkHttpClient()

    suspend fun downloadSong(url: String, destFile: File): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                val request = Request
                    .Builder()
                    .url(url)
                    .build()

                val response = httpClient
                    .newCall(request)
                    .execute()

                if (!response.isSuccessful) return@withContext false

                response.body?.byteStream()?.use { input ->
                    FileOutputStream(destFile).use { output ->
                        input.copyTo(output)
                    }
                }

                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }
    }
}