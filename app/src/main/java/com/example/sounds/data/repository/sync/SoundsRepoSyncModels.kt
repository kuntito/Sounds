package com.example.sounds.data.repository.sync

import com.example.sounds.data.remote.SongWithUrl

data class SyncCategories(
    val newSongs: List<SongWithUrl>,
    val existingSongs: List<SongWithUrl>,
    val missingSongs: Set<String>,
)