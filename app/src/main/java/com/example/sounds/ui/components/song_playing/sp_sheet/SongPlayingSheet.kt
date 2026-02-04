package com.example.sounds.ui.components.song_playing.sp_sheet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySong
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.song_playing.ControlSectionSongPlay
import com.example.sounds.ui.components.song_playing.DraggableSheetState
import com.example.sounds.ui.components.song_playing.SheetContainer
import com.example.sounds.ui.components.song_playing.sp_queue.SongPlayingQueueSheet
import com.example.sounds.ui.theme.colorAguero
import com.example.sounds.ui.theme.colorKDB



@Composable
fun rememberSheetState(
    minHeight: Int,
    maxHeight: Int,
): DraggableSheetState {
    val screenDensity = LocalDensity.current
    return remember(minHeight, maxHeight) {
        with(screenDensity) {
            val totalDragDistancePx = (maxHeight - minHeight).dp.toPx()
            DraggableSheetState(totalDragDistancePx)
        }
    }
}

/**
 * expandable sheet for currently playing song.
 *
 * collapsed: horizontal mini player bar showing album art, song metadata, and pause button.
 * expanded: full screen with centered album art, song metadata, and playback controls.
 *
 * album art is present in both states and animates between them.
 * metadata and controls only appear on full expansion.
 */
@Composable
fun SongPlayingSheet(
    modifier: Modifier = Modifier,
    miniPlayerHeight: Int,
    playerState: PlayerState,
    onPlay: (song: Song) -> Unit,
    onPause: () -> Unit,
    onSeekTo: (Float) -> Unit,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    songQueue: List<Song>,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sheetState = rememberSheetState(
        minHeight = miniPlayerHeight,
        maxHeight = screenHeight
    )

    val containerColor = if (sheetState.isCollapsed) colorAguero else colorKDB
    playerState.currentSong?.let { currentSong ->
        BackHandler(enabled = sheetState.isExpanded) {
            sheetState.collapse()
        }
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            val onPlaySong = { onPlay(currentSong) }
            SheetContainer(
                collapsedHeight = miniPlayerHeight,
                maxHeight = screenHeight,
                sheetState = sheetState,
                modifier = modifier
                    .background(color = containerColor),
            ) {
                // the padding above the image..
                if (sheetState.fractionOfSheetExpanded > 0f) {
                    Spacer(
                        modifier = Modifier.height(
                            lerp(
                                0.dp,
                                128.dp,
                                sheetState.fractionOfSheetExpanded
                            )
                        )
                    )
                }
                val horizontalPadding = 16
                Row(
                    modifier = Modifier
                ) {
                    ExpandableAlbumArtSP(
                        fractionOfSheetExpanded = sheetState.fractionOfSheetExpanded,
                        miniPlayerHeight = miniPlayerHeight,
                        horizontalPadding = horizontalPadding,
                        albumArtFilePath = currentSong.albumArtFilePath,
                    )
                    if(sheetState.isCollapsed) {
                        MiniPlayerSongPlaying(
                            heightDp = miniPlayerHeight,
                            endPaddingDp = horizontalPadding,
                            currentSong = currentSong,
                            isPlaying = playerState.isPlaying,
                            onPlay = onPlaySong,
                            onPause = onPause,
                            modifier = Modifier
                                .clickable(
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() },
                                    enabled = true,
                                    onClick = {
                                        sheetState.expand()
                                    }
                                )
                            ,
                        )
                    }
                }
                if (sheetState.isExpanded) {
                    ExpandedSheetComponents(
                        playerState = playerState,
                        onPlay = onPlaySong,
                        onPause = onPause,
                        onSeekTo = onSeekTo,
                        onPrev = onPrev,
                        onNext = onNext,
                        songQueue = songQueue,
                    )
                }
            }
            if (sheetState.isExpanded) {
                SongPlayingQueueSheet(
                    playerState = playerState,
                    songQueue = songQueue,
                )
            }
        }
    }
}

@Preview
@Composable
private fun SongPlayingSheetPreview() {
    Box(
        modifier = Modifier
            .background(colorKDB)
            .fillMaxSize(),
    ) {
        val currentSong = dummySongList[2]
        SongPlayingSheet(
            miniPlayerHeight = 48,
            playerState = PlayerState(
                currentSong = currentSong,
                isPlaying = true,
            ),
            onPlay = {},
            onPause = {},
            onSeekTo = {},
            onNext = {},
            onPrev = {},
            songQueue = dummySongList,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}



