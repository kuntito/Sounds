package com.example.sounds.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val SONGDB_MIGRATION_1_2 = object: Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE IF NOT EXISTS playlists (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE IF NOT EXISTS playlist_songs(
                playlistId INTEGER NOT NULL,
                songId TEXT NOT NULL,
                addedAt INTEGER NOT NULL DEFAULT 0,
                PRIMARY KEY (playlistId, songId),
                FOREIGN KEY (playlistId) REFERENCES playlists(id) ON DELETE CASCADE,
                FOREIGN KEY (songId) REFERENCES songs(id) ON DELETE CASCADE
            )
        """.trimIndent())
    }
}

val SONGDB_MIGRATION_2_3 = object: Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        db.execSQL("ALTER TABLE songs ADD COLUMN durationMillis INTEGER NOT NULL DEFAULT 0")
    }
}