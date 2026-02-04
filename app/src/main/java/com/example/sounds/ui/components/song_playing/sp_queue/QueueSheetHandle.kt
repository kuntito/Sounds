package com.example.sounds.ui.components.song_playing.sp_queue

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.components.song_playing.DraggableSheetState
import com.example.sounds.ui.theme.colorMarcelo
import com.example.sounds.ui.theme.colorTelli

@Composable
fun QueueSheetHandler(
    modifier: Modifier = Modifier,
    sheetState: DraggableSheetState,
    setIsHandlerTouched: (Boolean) -> Unit,
) {
    val handleColor = if (sheetState.isCollapsed) colorMarcelo else colorTelli


    Box(
        modifier = modifier
            .width(32.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(50))
            .background(color = handleColor)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        setIsHandlerTouched(true)
                        tryAwaitRelease()
                        setIsHandlerTouched(false)
                    },
                )
            }
        ,
    )
}