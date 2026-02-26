package com.example.sounds.ui.components.playlist_list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.R
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.PreviewColumn
import com.example.sounds.ui.theme.colorIsco

@Composable
fun NoPlaylistPrompt(
    modifier: Modifier = Modifier,
    onCreatePlaylistClick: () -> Unit,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
    ) {
        AppIconButton(
            iconRes = R.drawable.ic_playlist,
            size = 48,
            color = colorIsco,
        ) {
            onCreatePlaylistClick()
        }
        Spacer(modifier = Modifier.height(10.dp))
        CreatePlaylistButton(
            onClick = onCreatePlaylistClick,
        )
    }
}

@Preview
@Composable
private fun NoPlaylistPromptPreview() {
    PreviewColumn {
        NoPlaylistPrompt(
            onCreatePlaylistClick = {},
        )
    }
}