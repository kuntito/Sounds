package com.example.sounds.ui.components.song_playing.sp_queue

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.utils.PreviewColumn
import sh.calvin.reorderable.ReorderableItem
import sh.calvin.reorderable.rememberReorderableLazyListState


// TODO implement song drag within queue...

// TODO also, dragging this queue up isn't smooth
@Composable
fun SongPlayingQueue(
    modifier: Modifier = Modifier,
    isScrollEnabled: Boolean,
    playerState: PlayerState,
    songQueue: List<Song>,
    onSwapSong: (Int, Int) -> Unit,
) {
    val currentSongIdx = songQueue.indexOfFirst {
        it.id == playerState.currentSong?.id
    }
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (currentSongIdx == -1) 0 else currentSongIdx
    )

    val reorderableListState = rememberReorderableLazyListState(
        listState
    ) { from, to ->
        onSwapSong(from.index, to.index)
    }

    LazyColumn(
        state = listState,
        userScrollEnabled = isScrollEnabled,
        modifier = modifier,
    ) {

        items(songQueue, key = {it.id}) { song ->
            ReorderableItem(
                reorderableListState,
                key = song.id
            ) { isDragging ->
                SongQueueListItem(
                    title = song.title,
                    artistName = song.artistName,
                    albumArtFilePath = song.albumArtFilePath,
                    isCurrentSong = playerState.currentSong == song,
                    isSongPlaying = playerState.currentSong == song && playerState.isPlaying,
                    onClick = {},
                    dragHandleModifier = Modifier
                        .draggableHandle()
                )
            }
        }
    }
}

@Preview
@Composable
private fun SongPlayingQueuePreview() {
    PreviewColumn() {
        var songQueue by remember { mutableStateOf(dummySongList) }
        val currentSong = remember {  songQueue[2] }
        val playerState = PlayerState(
            currentSong = currentSong,
            isPlaying = true,
        )
        val onReorder: (Int, Int) -> Unit = { fromIndex, toIndex ->
            songQueue = songQueue.toMutableList().apply {
                add(toIndex, removeAt(fromIndex))
            }
        }

        SongPlayingQueue(
            isScrollEnabled = true,
            playerState = playerState,
            songQueue = songQueue,
            onSwapSong = onReorder,
        )
    }
}