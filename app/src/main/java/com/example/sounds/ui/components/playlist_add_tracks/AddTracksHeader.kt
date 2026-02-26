package com.example.sounds.ui.components.playlist_add_tracks

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.R
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorTelli
import com.example.sounds.ui.theme.tsOrion

@Composable
fun AddTracksHeader(
    modifier: Modifier = Modifier,
    hasSongs: Boolean,
    onAddFinished: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .height(48.dp)
            .fillMaxWidth()
        ,
    ) {
        val opacity by animateFloatAsState(
            targetValue = if (hasSongs) 1f else 0.15f,
            label = "checkmark opacity"
        )
        Spacer(modifier = Modifier.width(16.dp))
        AppIconButton(
            iconRes = R.drawable.ic_check,
            color = colorTelli.copy(alpha = opacity),
            isClickable = hasSongs,
        ) { }
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = "add tracks",
            style = tsOrion,
        )
        Spacer(modifier = Modifier.width(16.dp))
    }
}

@Preview
@Composable
private fun AddTracksHeaderPreview() {
    PreviewColumn {
        AddTracksHeader(
            onAddFinished = {},
            hasSongs = false,
        )
    }
}