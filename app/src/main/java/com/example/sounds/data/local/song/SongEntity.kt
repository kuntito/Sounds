package com.example.sounds.data.local.song

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val durationMillis: Int,
    val songFilePath: String,
    val albumArtFilePath: String,
)