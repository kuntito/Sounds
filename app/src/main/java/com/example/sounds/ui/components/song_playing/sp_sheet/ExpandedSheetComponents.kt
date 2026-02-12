package com.example.sounds.ui.components.song_playing.sp_sheet

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.song_playing.ControlSectionSongPlay
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun ExpandedSheetComponents(
    modifier: Modifier = Modifier,
    currentSong: Song?,
    playerState: PlayerState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onSeekTo: (Float) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    isSwipingToAnotherSong: Boolean = false,
    isShuffled: Boolean,
    toggleShuffle: () -> Unit,
    currentTrackNumber: Int,
    totalTracks: Int,
    spaceFromBottom: Int = 0,
) {
    currentSong?.let { currentSong ->
        val alpha by animateFloatAsState(
            targetValue = if (isSwipingToAnotherSong) 0f else 1f
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = modifier
                .fillMaxWidth()
                .alpha(alpha)
            ,
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            SongTitleSP(
                title = currentSong.title
            )
            Spacer(modifier = Modifier.height(8.dp))
            ArtistNameSongPlay(
                artistName = currentSong.artistName,
            )
            Spacer(modifier = Modifier.height(48.dp))
            ControlSectionSongPlay(
                playerState = playerState,
                onPlay = onPlay,
                onPause = onPause,
                onSeekTo = onSeekTo,
                onNext = onNext,
                onPrev = onPrev,
                isShuffled = isShuffled,
                toggleShuffle = toggleShuffle,
            )
            Spacer(modifier = Modifier.weight(1f))
            TrackPositionInQueue(
                currentTrackNumber = currentTrackNumber,
                totalTracks = totalTracks,
            )
            Spacer(modifier = Modifier.height(spaceFromBottom.dp))
        }
    }
}

@Preview
@Composable
private fun ExpandedSheetComponentsPreview() {
    PreviewColumn {
        val currentSong = dummySongList[2]
        ExpandedSheetComponents(
            currentSong = currentSong,
            playerState = PlayerState(
                isPlaying = true,
            ),
            onPlay = {},
            onPause = {},
            onSeekTo = {},
            onNext = {},
            onPrev = {},
            isShuffled = false,
            toggleShuffle = {},
            currentTrackNumber = 3,
            totalTracks = 12
        )
    }
}