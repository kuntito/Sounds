package com.example.sounds.ui.components.utils

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.theme.colorIsco
import com.example.sounds.ui.theme.colorTelli
import com.example.sounds.ui.theme.tsHush

@Composable
fun AppSnackBar(
    modifier: Modifier = Modifier,
    message: String,
) {
    val height = 24
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = modifier
            .height(height.dp)
            .clip(RoundedCornerShape((height/2).dp))
            .background(color = colorIsco)
        ,
    ) {
        Text(
            text = message,
            color = colorTelli,
            style = tsHush
                .copy(
                    fontWeight = FontWeight.Normal
                ),
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Preview
@Composable
private fun AppSnackBarPreview() {
    PreviewColumn {
        AppSnackBar(
            message = "Alubarika, added"
        )
    }
}