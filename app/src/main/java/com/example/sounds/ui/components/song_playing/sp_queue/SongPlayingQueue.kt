package com.example.sounds.ui.components.song_playing.sp_queue

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.sounds.data.models.Song
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.song_playing.DraggableSheetState

// TODO implement song drag within queue...

// TODO also, dragging this queue isn't smooth
@Composable
fun SongPlayingQueue(
    modifier: Modifier = Modifier,
    sheetState: DraggableSheetState,
    playerState: PlayerState,
    songQueue: List<Song>,
) {
    val currentSongIdx = songQueue.indexOfFirst {
        it.id == playerState.currentSong?.id
    }
    val listState = rememberLazyListState(
        initialFirstVisibleItemIndex = if (currentSongIdx == -1) 0 else currentSongIdx
    )

    LazyColumn(
        state = listState,
        userScrollEnabled = sheetState.isExpanded,
        modifier = modifier
    ) {
        itemsIndexed(songQueue) { index, song ->
            SongQueueListItem(
                title = song.title,
                artistName = song.artistName,
                albumArtFilePath = song.albumArtFilePath,
                isSongPlaying = playerState.currentSong == song && playerState.isPlaying,
                onClick = {}
            )
        }
    }
}