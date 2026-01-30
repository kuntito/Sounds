package com.example.sounds.ui.components.song_playing.sp_sheet

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySong
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.song_playing.ControlSectionSongPlay
import com.example.sounds.ui.components.utils.topShadow
import com.example.sounds.ui.theme.colorAguero
import com.example.sounds.ui.theme.colorKDB
import com.example.sounds.ui.theme.colorTelli

@Stable // Claude says @Stable avoids unnecessary recompositions
class SongPlayingSheetState(
    private val totalDragDistancePx: Float,
) {
    var fractionOfSheetExpanded by mutableFloatStateOf(0f)

    val isCollapsed: Boolean
        get() = fractionOfSheetExpanded == 0f

    val isExpanded: Boolean
        get() = fractionOfSheetExpanded == 1f

    fun onDrag(distancePx: Float) {
        val dragFraction = distancePx/ totalDragDistancePx
        fractionOfSheetExpanded = (fractionOfSheetExpanded - dragFraction).coerceIn(0f, 1f)
    }

    fun onDragEnd(dragVelocity: Float) {
        // pixels/sec
        val velocityThreshold = 1000f

        fractionOfSheetExpanded = when {
            dragVelocity < -velocityThreshold -> 1f // id swipe up fast, fully expand
            dragVelocity > velocityThreshold -> 0f // if swipe down fast, fully shrink
            fractionOfSheetExpanded >= 0.5f -> 1f
            else -> 0f
        }
    }

    fun expand() {
        fractionOfSheetExpanded = 1f
    }

    fun collapse() {
        fractionOfSheetExpanded = 0f
    }
}

@Composable
fun rememberSongPlayingSheetState(
    minHeight: Int,
    maxHeight: Int,
): SongPlayingSheetState {
    val screenDensity = LocalDensity.current
    return remember(minHeight, maxHeight) {
        with(screenDensity) {
            val totalDragDistancePx = (maxHeight - minHeight).dp.toPx()
            SongPlayingSheetState(totalDragDistancePx)
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
) {
    val screenHeight = LocalConfiguration.current.screenHeightDp
    val sheetState = rememberSongPlayingSheetState(
        minHeight = miniPlayerHeight,
        maxHeight = screenHeight
    )

    val containerColor = if (sheetState.isCollapsed) colorAguero else colorKDB
    playerState.currentSong?.let { currentSong ->

        val onPlaySong = { onPlay(currentSong) }
        SheetContainer(
            miniPlayerHeight = miniPlayerHeight,
            screenHeight = screenHeight,
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
            val shadowHeight = if (sheetState.isCollapsed) 20f else 0f
            // this is the horizontal mini player
            Row(
                modifier = Modifier
    //                .topShadow(shadowHeight = shadowHeight), pending fix
            ) {
                ExpandableAlbumArtSP(
                    fractionOfSheetExpanded = sheetState.fractionOfSheetExpanded,
                    miniPlayerHeight = miniPlayerHeight,
                    horizontalPadding = horizontalPadding,
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
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
                        onPlay = onPlaySong,
                        onPause = onPause,
                        onSeekTo = onSeekTo,
                    )
                }
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
        SongPlayingSheet(
            miniPlayerHeight = 48,
            playerState = PlayerState(
                currentSong = dummySong,
            ),
            onPlay = {},
            onPause = {},
            onSeekTo = {},
            modifier = Modifier
                .align(Alignment.BottomCenter)
        )
    }
}


@Composable
private fun SheetContainer(
    miniPlayerHeight: Int,
    screenHeight: Int,
    sheetState: SongPlayingSheetState,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(
                lerp(
                    miniPlayerHeight.dp,
                    screenHeight.dp,
                    sheetState.fractionOfSheetExpanded,
                )
            )
            .draggable(
                state = rememberDraggableState { pixelsDragged ->
                    sheetState.onDrag(pixelsDragged)
                },
                orientation = Orientation.Vertical,
                onDragStopped = { velocity -> sheetState.onDragEnd(velocity) }
            ),
    ) {
        content()
    }
}



