package com.example.sounds.ui.components.playlist_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.DropdownMenuOption
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.ui.components.utils.AppSnackBar
import com.example.sounds.ui.components.utils.PreviewColumn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

@Composable
fun PlaylistSongList(
    modifier: Modifier = Modifier,
    topEdgePadding: Float,
    bottomEdgePadding: Float,
    playlistSongs: List<Song>,
    onSongItemClick: (Int, List<Song>) -> Unit,
    getPlaylistSongMenuOptions: (Song) -> List<DropdownMenuOption>,
    onRemoveSongSuccess: Flow<String>,
) {
    val snackBarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        onRemoveSongSuccess.collect { songTitle ->
            snackBarHostState.showSnackbar(
                message = "$songTitle removed",
                duration = SnackbarDuration.Short,
            )
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize(),
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
                    playlistSongMenuOptions = getPlaylistSongMenuOptions(song),
                    onClick = {
                        onSongItemClick(index, playlistSongs)
                    },
                )
            }
            item {
                Spacer(
                    modifier = Modifier
                        .height(bottomEdgePadding.dp)
                )
            }
        }
        SnackbarHost(
            hostState = snackBarHostState,
            snackbar = { data ->
                AppSnackBar(
                    message = data.visuals.message,
                )
            },
            modifier = Modifier
                .align(
                    Alignment.TopCenter
                )
        )
    }
}

@Preview
@Composable
private fun PlaylistSongListPreview() {
    PreviewColumn {
        PlaylistSongList(
            playlistSongs = dummySongList,
            topEdgePadding = 16f,
            bottomEdgePadding = 16f,
            getPlaylistSongMenuOptions = { emptyList() },
            onSongItemClick = { _, _ -> },
            onRemoveSongSuccess = MutableSharedFlow(),
        )
    }
}