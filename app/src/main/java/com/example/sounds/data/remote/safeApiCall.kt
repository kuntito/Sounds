package com.example.sounds.data.remote

import android.util.Log
import com.example.sounds.soundsDebugTag


/**
 * info about an api call.
 *
 * fnDesc describes what the call does, used in logs when the call fails.
 * fn is the actual api call.
 */
data class ApiCallInfo<T>(
    val fnDesc: String,
    val fn: suspend () -> T
)

/**
 * wraps an API call in a try-catch block.
 *
 * if the call fails, it logs the error with the description from ApiCallInfo and returns null.
 * if it succeeds, it returns the result.
 */
suspend fun <T> safeApiCall(
    apiInfo: ApiCallInfo<T>
): T? {
    return try {
        apiInfo.fn()
    } catch (e: Exception) {
        Log.d(
            soundsDebugTag,
            "api call fails, ${apiInfo.fnDesc}, it fails with '$e'"
        )
        null
    }
}