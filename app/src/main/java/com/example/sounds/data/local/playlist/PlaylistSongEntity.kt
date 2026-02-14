package com.example.sounds.data.local.playlist

import androidx.room.Entity
import androidx.room.ForeignKey
import com.example.sounds.data.local.song.SongEntity

@Entity(
    tableName = "playlist_songs",
    primaryKeys = ["playlistId", "songId"],
    foreignKeys = [
        ForeignKey(
            entity = PlaylistEntity::class,
            parentColumns = ["id"],
            childColumns = ["playlistId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = SongEntity::class,
            parentColumns = ["id"],
            childColumns = ["songId"],
            onDelete = ForeignKey.CASCADE,
        )
    ]
)
data class PlaylistSongEntity(
    val playlistId: Long,
    val songId: String,
    val addedAt: Long = System.currentTimeMillis()
)