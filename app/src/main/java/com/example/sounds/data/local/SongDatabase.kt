package com.example.sounds.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SongEntity::class
    ],
    version = 1,
)
abstract class SoundsDb: RoomDatabase() {
    abstract fun songDao(): SongDao

    companion object {
        @Volatile
        private var INSTANCE: SoundsDb? = null

        fun getDatabase(context: Context): SoundsDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SoundsDb::class.java,
                    "sounds_db"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}