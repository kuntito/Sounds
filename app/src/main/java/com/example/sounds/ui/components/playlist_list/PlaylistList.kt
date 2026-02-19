package com.example.sounds.ui.components.playlist_list

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Playlist
import com.example.sounds.data.models.dummyPlaylistList
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun PlaylistList(
    modifier: Modifier = Modifier,
    playlists: List<Playlist>,
    topEdgePadding: Float,
    bottomEdgePadding: Float,
    onPlaylistClick: (Long) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = topEdgePadding.dp,
            bottom = bottomEdgePadding.dp
        ),
        modifier = modifier
            .fillMaxSize()
        ,
    ) {
        itemsIndexed(playlists) { index, pl ->
            PlaylistListItem(
                playlistName = pl.playlistName,
                onClick = {
                    onPlaylistClick(pl.id)
                }
            )
        }
    }
}

@Preview
@Composable
private fun PlaylistListPreview() {
    PreviewColumn() {
        PlaylistList(
            playlists = dummyPlaylistList,
            topEdgePadding = 16f,
            bottomEdgePadding = 16f,
            onPlaylistClick = {},
        )
    }
}