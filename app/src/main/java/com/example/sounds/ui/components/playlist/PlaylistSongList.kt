package com.example.sounds.ui.components.playlist

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun PlaylistSongList(
    modifier: Modifier = Modifier,
    topEdgePadding: Float,
    bottomEdgePadding: Float,
    playlistSongs: List<Song>,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
    ) {
        item {
            Spacer(
                modifier = Modifier
                    .height(topEdgePadding.dp)
            )
        }
        itemsIndexed(playlistSongs) { index, song ->
            PlaylistSongListItem(
                title = song.title,
                artistName = song.artistName,
                albumArtFilePath = song.albumArtFilePath,
                onClick = {},
            )
        }
        item {
            Spacer(
                modifier = Modifier
                    .height(bottomEdgePadding.dp)
            )
        }
    }
}

@Preview
@Composable
private fun PlaylistSongListPreview() {
    PreviewColumn {
        PlaylistSongList(
            playlistSongs = dummySongList,
            topEdgePadding = 16f,
            bottomEdgePadding = 16f
        )
    }
}