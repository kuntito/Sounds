package com.example.sounds.utils

/**
 * takes milliseconds and returns whole minutes, dropping the remainder.
 */
fun millisToMinutes(millis: Int): Int {
    val millisPerSecond = 1000
    val secondsPerMinute = 60
    val minutes = millis / millisPerSecond / secondsPerMinute

    return minutes
}