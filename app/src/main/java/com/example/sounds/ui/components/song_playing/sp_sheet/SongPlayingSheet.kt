package com.example.sounds.ui.components.song_playing.sp_sheet

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.song_playing.DraggableSheetState
import com.example.sounds.ui.components.song_playing.SheetContainer
import com.example.sounds.ui.components.song_playing.SwipeableAlbumArt
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
    currentSong: Song?,
    prevSongAAFP: String?,
    nextSongAAFP: String?,
    onSwapSong: (Int, Int) -> Unit,
    onSongItemClick: (Int, List<Song>) -> Unit,
    isShuffled: Boolean,
    toggleShuffle: () -> Unit,
    currentTrackNumber: Int,
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sheetState = rememberSheetState(
        minHeight = miniPlayerHeight,
        maxHeight = screenHeight
    )

    val containerColor = if (sheetState.isCollapsed) colorAguero else colorKDB

    val songPlayingQueueCollapsedHeight = 48
    currentSong?.let { currentSong ->
        BackHandler(enabled = sheetState.isExpanded) {
            sheetState.collapse()
        }

        var isSwipingToAnotherSong by remember { mutableStateOf(false) }
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
                Row(
                    modifier = Modifier
                ) {
                    val horizontalPadding = 16
                    val screenWidth = LocalConfiguration.current.screenWidthDp
                    val minImageSize = 32
                    val maxImageSize = 256
                    SwipeableAlbumArt(
                        currentAAFP = currentSong.albumArtFilePath,
                        truePrevAAFP = prevSongAAFP,
                        trueNextAAFP = nextSongAAFP,
                        onSwipeNextSong = onNext,
                        onSwipePrevSong = onPrev,
                        isSwipeEnabled = sheetState.isExpanded,
                        maxImageSize = maxImageSize,
                        onSwiping = { isSwipingToAnotherSong = it },
                        modifier = Modifier
                            .albumArtAnimatedModifier(
                                    horizontalPadding = horizontalPadding,
                                    miniPlayerHeight = miniPlayerHeight,
                                    minImageSize = minImageSize,
                                    screenWidth = screenWidth,
                                    maxImageSize = maxImageSize,
                                    fractionExpanded = sheetState.fractionOfSheetExpanded,
                                )
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
                        isSwipingToAnotherSong = isSwipingToAnotherSong,
                        currentSong = currentSong,
                        isShuffled = isShuffled,
                        toggleShuffle = toggleShuffle,
                        currentTrackNumber = currentTrackNumber,
                        totalTracks = songQueue.size,
                        spaceFromBottom = songPlayingQueueCollapsedHeight,
                    )
                }
            }
            if (sheetState.isExpanded) {
                SongPlayingQueueSheet(
                    sheetCollapsedHeight = songPlayingQueueCollapsedHeight,
                    playerState = playerState,
                    songQueue = songQueue,
                    onSwapSong = onSwapSong,
                    currentSong = currentSong,
                    onSongItemClick = onSongItemClick
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
                isPlaying = true,
            ),
            onPlay = {},
            onPause = {},
            onSeekTo = {},
            onNext = {},
            onPrev = {},
            songQueue = dummySongList,
            currentSong = currentSong,
            onSwapSong = { _, _ ->  },
            prevSongAAFP = null,
            nextSongAAFP = null,
            onSongItemClick = { _, _ -> },
            isShuffled = false,
            toggleShuffle = {},
            currentTrackNumber = 3,
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}

/**
 * the album art is part of the `SongPlayingSheet`, it starts in a mini player
 *
 * this mini player appears at the bottom of the screen.
 *
 * in this state, the album art component has padding on left and right sides.
 * as the sheet expands, it enlarges and the padding reduces to zero.
 *
 * note: the album art stays centered even at `0dp` padding because
 * its parent component handles the centering when sheet is fully expanded.
 */
private fun Modifier.albumArtAnimatedModifier(
    horizontalPadding: Int,
    miniPlayerHeight: Int,
    minImageSize: Int,
    screenWidth: Int,
    maxImageSize: Int,
    fractionExpanded: Float,
): Modifier = this
    .padding(
        // horizontal padding animates from left-aligned to center position.
        // centering achieved by calculating padding needed:
        // `(screenWidth - imageSize) / 2`
        horizontal = lerp(
            horizontalPadding.dp,
            0.dp,
            fractionExpanded,
        ),
        vertical = lerp(
            start = ((miniPlayerHeight - minImageSize) / 2).dp,
            stop = 0.dp,
            fractionExpanded,
        )
    )
    .width(
        lerp(
            minImageSize.dp,
            screenWidth.dp,
            fractionExpanded
        )
    )
    .height(
        lerp(
            minImageSize.dp,
            maxImageSize.dp,
            fractionExpanded
        )
    )