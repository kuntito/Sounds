package com.example.sounds.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sounds.data.local.playlist.PlaylistDao
import com.example.sounds.data.local.song.SongDao
import com.example.sounds.data.local.song.SongEntity
import com.example.sounds.data.local.playlist.PlaylistEntity
import com.example.sounds.data.local.playlist.PlaylistSongEntity

@Database(
    entities = [
        SongEntity::class,
        PlaylistEntity::class,
        PlaylistSongEntity::class,
    ],
    version = 3,
)
abstract class SoundsDb: RoomDatabase() {
    abstract fun songDao(): SongDao
    abstract fun playlistDao(): PlaylistDao

    companion object {
        @Volatile
        private var INSTANCE: SoundsDb? = null

        fun getDatabase(context: Context): SoundsDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SoundsDb::class.java,
                    "sounds_db"
                )
                    .addMigrations(SONGDB_MIGRATION_1_2)
                    .addMigrations(SONGDB_MIGRATION_2_3)
                    .build()

                INSTANCE = instance
                instance
            }
        }
    }
}