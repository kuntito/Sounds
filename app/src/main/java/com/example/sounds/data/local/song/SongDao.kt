package com.example.sounds.data.local.song

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM songs")
    fun getAllSongs(): Flow<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(song: SongEntity)

    @Query("SELECT songFilePath FROM songs WHERE id = :songId")
    suspend fun getSongFilePath(songId: String): String?
}