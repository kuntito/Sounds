package com.example.sounds.ui.components.song_playing

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.player.PlaybackActions
import com.example.sounds.player.PlayerState
import com.example.sounds.player.dummyPlaybackActions
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun ControlButtonsSongPlay(
    modifier: Modifier = Modifier,
    width: Float = 256f,
    playerState: PlayerState,
    playbackActions: PlaybackActions,
    onPlay: () -> Unit,
    isShuffled: Boolean,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .width(width.dp)
        ,
    ) {

        ShuffleBtn(
            isShuffled = isShuffled,
            toggleShuffle = playbackActions.toggleShuffle,
        )
        PrevPlayPauseNextBtns(
            playerState = playerState,
            onPlay = onPlay,
            onPause = playbackActions.onPause,
            onNext = playbackActions.onNext,
            onPrev = playbackActions.onPrev,
        )
        RepeatBtn()
    }
}


@Preview
@Composable
private fun ControlButtonsSongPlayPreview() {
    PreviewColumn() {
        ControlButtonsSongPlay(
            playerState = PlayerState(
                isPlaying = false,
            ),
            onPlay = {},
            playbackActions = dummyPlaybackActions,
            isShuffled = false,
        )
    }
}