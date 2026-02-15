package com.example.sounds.ui

sealed class HomeScreenTabs(val title: String) {
    data object TrackList: HomeScreenTabs("tracks")
    data object Playlists: HomeScreenTabs("playlists")

    companion object {
        val allTabs = listOf(
            TrackList,
            Playlists,
        )
    }
}