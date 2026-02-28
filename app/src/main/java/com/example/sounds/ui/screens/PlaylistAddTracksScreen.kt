package com.example.sounds.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.dummySongList
import com.example.sounds.playlist.AddTracksManager
import com.example.sounds.ui.SongViewModel
import com.example.sounds.ui.components.playlist_add_tracks.AddTrackSearchBar
import com.example.sounds.ui.components.playlist_add_tracks.AddTracksHeader
import com.example.sounds.ui.components.playlist_add_tracks.PlaylistAddTracksPool
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun PlaylistAddTracksScreenRoot(
    modifier: Modifier = Modifier,
    songViewModel: SongViewModel,
    onFinishCreatePlaylist: () -> Unit,
) {
    val addTracksManager by songViewModel.addTracksManager.collectAsState()

    addTracksManager?.let {
        PlaylistAddTracksScreen(
            addTracksManager = it,
            onAddFinished = onFinishCreatePlaylist,
            modifier = modifier,
        )
    }
}

@Composable
fun PlaylistAddTracksScreen(
    modifier: Modifier = Modifier,
    addTracksManager: AddTracksManager,
    onAddFinished: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        val songPool by addTracksManager.exposedPool.collectAsState()
        AddTracksHeader(
            onAddFinished = onAddFinished,
            hasSongs = addTracksManager.hasSongs,
        )
        AddTrackSearchBar(
            onQueryChange = addTracksManager::onSearchSong,
        )
        PlaylistAddTracksPool(
            addedTracksIds = addTracksManager.addedTracksIds,
            pool = songPool,
            onAddTrack = addTracksManager::addSong,
            modifier = modifier
        )
    }
}

@Preview
@Composable
private fun PlaylistAddTracksScreenPreview() {
    PreviewColumn {
        PlaylistAddTracksScreen(
            addTracksManager = AddTracksManager(
                dummySongList
            ),
            onAddFinished = {},
        )
    }
}