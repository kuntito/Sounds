package com.example.sounds.ui.components.playlist_add_tracks

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.ui.components.utils.AppSnackBar
import com.example.sounds.ui.components.utils.ClickableSurface
import com.example.sounds.ui.components.utils.PreviewColumn
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

/**
* shows a list of songs that can be added to a playlist.
*
* on item click, the caller adds the song to playlist, then
* notifies this component by sending the song id.
*
* the component then animates out the song item from the list.
* and displays a snackbar message.
*
* if a new list is received, the component resets.
**/
@Composable
fun PlaylistAddTracksPool(
    modifier: Modifier = Modifier,
    addedTracksIds: Flow<String>,
    pool: List<Song>,
    onAddTrack: (Song) -> Unit,
) {
    var songIdsInPool by remember {
        mutableStateOf(pool.map{ it.id }.toSet())
    }

    LaunchedEffect(addedTracksIds) {
        addedTracksIds.collect { id ->
            songIdsInPool -= id
        }
    }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val onItemClick: (Song) -> Unit = { song ->
        onAddTrack(song)
        scope.launch {
            snackBarHostState.currentSnackbarData?.dismiss()
            snackBarHostState.showSnackbar(
                message = "${song.title}, added",
                duration = SnackbarDuration.Short,
            )
        }
    }

    val animationDurationMillis = 500

    LaunchedEffect(pool) {
        songIdsInPool = pool.map{ it.id }.toSet()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            contentPadding = PaddingValues(vertical = 16.dp),
            modifier = Modifier
            ,
        ) {
            items(
                items = pool,
                key = { it.id }
            ) { song ->
                AnimatedVisibility(
                    visible = song.id in songIdsInPool,
                    exit = shrinkVertically(
                        animationSpec = tween(durationMillis = animationDurationMillis)
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = animationDurationMillis)
                    )
                ) {
                    ClickableSurface(
                        onClick = { onItemClick(song) },
                        isRippleBounded = true,
                    ) {
                        PlaylistAddTrackListItem(
                            title = song.title,
                            artistName = song.artistName,
                            albumArtFilePath = song.albumArtFilePath,
                        )
                    }
                }
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
                    Alignment.TopCenter,
                )
            ,
        )
    }
}

@Preview
@Composable
private fun PlaylistAddTracksPoolPreview() {
    PreviewColumn() {
        val addedTracksIds = MutableStateFlow("")
        var pool by remember { mutableStateOf(dummySongList) }
        val onAddTrack: (Song) -> Unit = {
            addedTracksIds.tryEmit(
                it.id
            )
        }

        Button(
            onClick = {
                pool = dummySongList.shuffled().take(5)
            }
        ) { Text(text = "swap pool") }

        PlaylistAddTracksPool(
            pool = pool,
            addedTracksIds = addedTracksIds,
            onAddTrack = onAddTrack,
        )
    }
}