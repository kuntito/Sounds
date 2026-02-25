package com.example.sounds.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.components.playlist_add_tracks.AddTrackSearchBar
import com.example.sounds.ui.components.playlist_add_tracks.AddTracksHeader
import com.example.sounds.ui.components.playlist_add_tracks.AddTracksSearchResultList
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun PlaylistAddTracksScreenRoot(
    modifier: Modifier = Modifier,
    songViewModel: SongViewModel,
) {
    val searchResults = emptyList<Song>()
    PlaylistAddTracksScreen(
        searchResults = searchResults,
        modifier = modifier,
    )
}

@Composable
fun PlaylistAddTracksScreen(
    modifier: Modifier = Modifier,
    searchResults: List<Song>,
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
        )
    }
}