package com.example.sounds.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlaybackActions
import com.example.sounds.player.PlaybackRepeatModes
import com.example.sounds.player.PlayerState
import com.example.sounds.player.dummyPlaybackActions
import com.example.sounds.ui.components.song_list.SongList
import com.example.sounds.ui.components.song_playing.sp_sheet.SongPlayingSheet
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun SongPlayingScreen(
    songs: List<Song>,
    playerState: PlayerState,
    playbackActions: PlaybackActions,
    currentSong: Song?,
    songQueue: List<Song>,
    isShuffled: Boolean,
    playbackRepeatMode: PlaybackRepeatModes,
    currentTrackNumber: Int,
    prevSongAAFP: String?,
    nextSongAAFP: String?,
    modifier: Modifier = Modifier,
) {

    val miniPlayerHeight = 48
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        SongList(
            songList = songs,
            topEdgePadding = 16f,
            bottomEdgePadding = miniPlayerHeight * 1.2f,
            playerState = playerState,
            onSongItemClick = playbackActions.onSongItemClick,
            currentSong = currentSong,
        )
        AnimatedVisibility(
            visible = currentSong != null,
            modifier = Modifier
                .align(Alignment.BottomCenter),
        ) {
            SongPlayingSheet(
                miniPlayerHeight = miniPlayerHeight,
                playerState = playerState,
                playbackActions = playbackActions,
                songQueue = songQueue,
                isShuffled = isShuffled,
                playbackRepeatMode = playbackRepeatMode,
                currentSong = currentSong,
                prevSongAAFP = prevSongAAFP,
                nextSongAAFP = nextSongAAFP,
                currentTrackNumber = currentTrackNumber,
            )
        }
    }
}

@Preview
@Composable
private fun SongPlayingScreenPreview() {
    PreviewColumn(
        enablePadding = false
    ) {
        SongPlayingScreen(
            songs = dummySongList,
            playerState = PlayerState(),
            playbackActions = dummyPlaybackActions,
            songQueue = dummySongList,
            isShuffled = false,
            playbackRepeatMode = PlaybackRepeatModes.NoRepeat,
            currentSong = null,
            prevSongAAFP = null,
            nextSongAAFP = null,
            currentTrackNumber = 3,
        )
    }
}