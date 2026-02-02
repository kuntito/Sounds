package com.example.sounds.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val artist: String,
    val songFilePath: String,
    val albumArtFilePath: String,
)