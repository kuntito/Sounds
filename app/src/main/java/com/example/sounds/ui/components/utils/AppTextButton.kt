package com.example.sounds.ui.components.utils

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.theme.tsOrion

@Composable
fun AppTextButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: () -> Unit,
) {
    ClickableSurface(
        onClick = onClick,
        isRippleBounded = true,
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
        ,
    ) {
        Text(
            text = text,
            style = tsOrion,
            modifier = Modifier
                .padding(
                    vertical = 8.dp,
                    horizontal = 8.dp,
                )
            ,
        )
    }
}

@Preview
@Composable
private fun AppTextButtonPreview() {
    PreviewColumn {
        AppTextButton(
            text = "vibes"
        ) { }
    }
}