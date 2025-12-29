package com.example.sounds.ui.components.song_playing

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsDraggedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorAguero
import com.example.sounds.ui.theme.colorTelli


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeekBar(
    modifier: Modifier = Modifier,
    width: Float = 256f,
) {
    var progress by remember { mutableFloatStateOf(0f) }
    val onProgressChange: (Float) -> Unit = {
        progress = it
    }

    val colors = SliderDefaults.colors(
        thumbColor = colorTelli,
        activeTrackColor = colorTelli,
        inactiveTrackColor = colorAguero
    )

    val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isDragged by interactionSource.collectIsDraggedAsState()

    // thumb only visible when touched
    val showThumb = isPressed || isDragged
    val thumbColor = if (showThumb) colorTelli.copy(alpha = 0.5f) else Color.Transparent

    Slider(
        value = progress,
        onValueChange = onProgressChange,
        colors = colors,
        interactionSource = interactionSource,
        track = { sliderState ->
            SliderDefaults.Track(
                colors = colors,
                sliderState = sliderState,
                drawStopIndicator = null,
                thumbTrackGapSize = 0.dp,
                modifier = Modifier
                    .height(4.dp),
            )
        },
        thumb = {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .background(
                        color = thumbColor,
                        shape = CircleShape
                    )
            )
        },
        modifier = modifier
            .width(width.dp)
    )
}

@Preview
@Composable
private fun SeekBarPreview() {
    PreviewColumn{
        SeekBar()
    }
}