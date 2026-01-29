package com.example.sounds.data.remote

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object SoundsApiClient {
    // TODO replace with stable base url
    private const val BASE_URL = "https://05ef49e3e7a9.ngrok-free.app"

    // JSON parser with Kotlin support, apparently, JS has this in-built, so i didn't have to do it
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    // allows to log requests and responses in Logcat
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val soundsApi: SoundsApi = retrofit.create(SoundsApi::class.java)
}

