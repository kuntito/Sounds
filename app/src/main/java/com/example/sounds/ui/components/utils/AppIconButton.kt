package com.example.sounds.ui.components.utils

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.R

@Composable
fun AppIconButton(
    color: Color = Color.Unspecified,
    @DrawableRes iconRes: Int,
    size: Int = 24,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    ClickableSurface(
        onClick = onClick,
        rippleRadius = size * 1.05f,
        modifier = modifier
        ,
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = color,
            modifier = Modifier
                .size(size.dp)
            ,
        )
    }
}

@Preview
@Composable
private fun AppIconButtonPreview() {
    PreviewColumn() {
        AppIconButton(
            size = 100,
            iconRes = R.drawable.ic_more_vert,
        ){}
    }
}