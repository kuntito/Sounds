package com.example.sounds.ui.components.song_playing.sp_queue

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.data.models.Song
import com.example.sounds.data.models.dummySongList
import com.example.sounds.player.PlayerState
import com.example.sounds.ui.components.song_playing.SheetContainer
import com.example.sounds.ui.components.song_playing.sp_sheet.rememberSheetState
import com.example.sounds.ui.theme.colorAguero
import com.example.sounds.ui.theme.colorKDB

@Composable
fun SongPlayingQueueSheet(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    currentSong: Song?,
    songQueue: List<Song>,
    onSwapSong: (Int, Int) -> Unit,
    onSongItemClick: (Int, List<Song>) -> Unit,
) {
    val minHeight = 48
    val screenHeight = LocalConfiguration.current.screenHeightDp

    val sheetState = rememberSheetState(
        minHeight = minHeight,
        maxHeight = screenHeight
    )

    BackHandler(
        enabled = sheetState.isExpanded
    ) {
        sheetState.collapse()
    }

    val sheetCornerRadius = 16.dp
    var isHandlerTouched by remember { mutableStateOf(false) }

    SheetContainer(
        collapsedHeight = minHeight,
        maxHeight = screenHeight,
        sheetState = sheetState,
        modifier = modifier
            .then(
                if (isHandlerTouched || !sheetState.isCollapsed) {
                    Modifier
                        .clip(
                            RoundedCornerShape(
                                topStart = sheetCornerRadius,
                                topEnd = sheetCornerRadius
                            )
                        )
                        .background(colorAguero)
                } else Modifier
            )
        ,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(minHeight.dp)
                    .fillMaxWidth()
                ,
            ) {
                QueueSheetHandler(
                    sheetState = sheetState,
                    setIsHandlerTouched = {
                        isHandlerTouched = it
                    }
                )
            }
            SongPlayingQueue(
                isScrollEnabled = sheetState.isExpanded,
                playerState = playerState,
                currentSong = currentSong,
                songQueue = songQueue,
                onSwapSong = onSwapSong,
                onSongItemClick = onSongItemClick,
            )

        }
    }
}


@Preview
@Composable
private fun SongPlayingQueueSheetPreview() {
    Box(
        modifier = Modifier
            .background(colorKDB)
            .fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        SongPlayingQueueSheet(
            playerState = PlayerState(
                isPlaying = true,
            ),
            currentSong = dummySongList[3],
            songQueue = dummySongList,
            onSwapSong = { _, _ -> },
            onSongItemClick = { _, _ -> }
        )
    }
}