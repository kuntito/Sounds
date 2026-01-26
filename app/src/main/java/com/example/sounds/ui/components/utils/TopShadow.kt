package com.example.sounds.ui.components.utils

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

fun Modifier.topShadow(
    shadowHeight: Float,
    startColor: Color = Color.Transparent,
    endColor: Color = Color.Black
) = this.drawWithContent {
    drawRect(
        brush = Brush.verticalGradient(
            colors = listOf(
                endColor,
                startColor,
            ),
            startY = -shadowHeight,
            endY = 0f
        ),
        topLeft = Offset(0f, -shadowHeight),
        size = Size(size.width, shadowHeight)
    )
    drawContent()
}