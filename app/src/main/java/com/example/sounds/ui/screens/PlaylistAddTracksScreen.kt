package com.example.sounds.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.ui.components.playlist_add_tracks.AddTrackSearchBar
import com.example.sounds.ui.components.playlist_add_tracks.AddTracksHeader
import com.example.sounds.ui.components.playlist_add_tracks.AddTracksSearchResultList
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun PlaylistAddTracksScreen(
    modifier: Modifier = Modifier,
    searchResults: List<Song>,
    topEdgePadding: Float,
    bottomEdgePadding: Float,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        AddTracksHeader(
            onAddFinished = {}
        )
        AddTrackSearchBar(
            onQueryChange = {},
        )
        AddTracksSearchResultList(
            topEdgePadding = topEdgePadding,
            bottomEdgePadding = bottomEdgePadding,
            songSearchResults = searchResults,
            onAddTrack = {},
        )
    }
}

@Preview
@Composable
private fun PlaylistAddTracksScreenPreview() {
    PreviewColumn {
        PlaylistAddTracksScreen(
            searchResults = dummySongList,
            topEdgePadding = 16f,
            bottomEdgePadding = 16f,
        )
    }
}