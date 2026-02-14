package com.example.sounds.ui.components.song_playing

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.player.PlaybackActions
import com.example.sounds.player.PlaybackRepeatModes
import com.example.sounds.player.PlayerState
import com.example.sounds.player.dummyPlaybackActions
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun ControlSectionSongPlay(
    modifier: Modifier = Modifier,
    width: Float = 256f,
    playerState: PlayerState,
    playbackActions: PlaybackActions,
    onPlaySong: () -> Unit,
    isShuffled: Boolean,
    playbackRepeatMode: PlaybackRepeatModes
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
        ,
    ) {
        SeekBar(
            width = width * 1.07f, // looks better this way
            progress = playerState.playProgress,
            durationMs = playerState.durationMs,
            onSeekTo = playbackActions.onSeekTo,
        )
        Spacer(
            modifier = Modifier
                .height(32.dp)
        )
        ControlButtonsSongPlay(
            width = width,
            playerState = playerState,
            playbackActions = playbackActions,
            onPlay = onPlaySong,
            isShuffled = isShuffled,
            playbackRepeatMode = playbackRepeatMode,
        )
    }
}

@Preview
@Composable
private fun ControlSectionSongPlayPreview() {
    PreviewColumn() {
        ControlSectionSongPlay(
            playerState = PlayerState(
                isPlaying = false,
            ),
            playbackActions = dummyPlaybackActions,
            onPlaySong = {},
            isShuffled = false,
            playbackRepeatMode = PlaybackRepeatModes.NoRepeat,
        )
    }
}