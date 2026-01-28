package com.example.sounds.ui.components.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.drawscope.Stroke

// FIXME, it still shows the edge of the rectangle..
fun Modifier.topShadow(
    shadowHeight: Float,
    startColor: Color = Color.Transparent,
    endColor: Color = Color.Black
) = this
    .graphicsLayer {
        clip = false
    }
    .drawWithContent {
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    endColor,
                    startColor,
                ),
                startY = -shadowHeight * 1.2f,
                endY = 0f
            ),
            topLeft = Offset(0f, -shadowHeight),
            size = Size(size.width, shadowHeight)
        )
        drawContent()
    }