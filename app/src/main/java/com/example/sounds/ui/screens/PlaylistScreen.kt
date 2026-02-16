package com.example.sounds.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Playlist
import com.example.sounds.data.models.dummyPlaylistList
import com.example.sounds.ui.components.playlist.CreatePlaylistButton
import com.example.sounds.ui.components.playlist.NoPlaylistPrompt
import com.example.sounds.ui.components.playlist.PlaylistList
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    playlists: List<Playlist>,
    onPlaylistClick: (Long) -> Unit,
    onCreatePlaylistClick: () -> Unit,
    miniPlayerHeight: Int,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        if (playlists.isEmpty()) {
            NoPlaylistPrompt(
                onCreatePlaylistClick = onCreatePlaylistClick,
            )
        } else {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                ,
            ) {
                CreatePlaylistButton(
                    onClick = onCreatePlaylistClick,
                )
            }
            PlaylistList(
                playlists = playlists,
                topEdgePadding = 16f,
                // TODO unify bottomEdgePadding, it also occurs in `TrackListScreen`
                bottomEdgePadding = miniPlayerHeight * 1.2f,
                onPlaylistClick = onPlaylistClick,
            )
        }
    }
}

@Preview
@Composable
private fun PlaylistScreenPreview() {
    PreviewColumn(
        enablePadding = false
    ) {
        PlaylistScreen(
            playlists = dummyPlaylistList,
            onPlaylistClick = {},
            onCreatePlaylistClick = {},
            miniPlayerHeight = 48,
        )
    }
}