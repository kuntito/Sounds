package com.example.sounds.ui.components.song_playing

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp

@Stable // Claude says @Stable avoids unnecessary recompositions
class DraggableSheetState(
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
fun SheetContainer(
    collapsedHeight: Int,
    maxHeight: Int,
    sheetState: DraggableSheetState,
    modifier: Modifier = Modifier.Companion,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(
                lerp(
                    collapsedHeight.dp,
                    maxHeight.dp,
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