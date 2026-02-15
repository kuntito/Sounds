package com.example.sounds.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlaybackActions
import com.example.sounds.player.PlayerState
import com.example.sounds.player.dummyPlaybackActions
import com.example.sounds.ui.components.song_list.SongList
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun TrackListScreen(
    songs: List<Song>,
    playerState: PlayerState,
    playbackActions: PlaybackActions,
    currentSong: Song?,
    bottomEdgePadding: Int,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        SongList(
            songList = songs,
            topEdgePadding = 16f,
            bottomEdgePadding = bottomEdgePadding * 1.2f,
            playerState = playerState,
            onSongItemClick = playbackActions.onSongItemClick,
            currentSong = currentSong,
        )
    }
}

@Preview
@Composable
private fun TrackListScreenPreview() {
    PreviewColumn(
        enablePadding = false
    ) {
        TrackListScreen(
            songs = dummySongList,
            playerState = PlayerState(),
            playbackActions = dummyPlaybackActions,
            currentSong = null,
            bottomEdgePadding = 0,
        )
    }
}