package com.example.sounds.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) { }
}

@Preview
@Composable
private fun PlaylistScreenPreview() {
    PreviewColumn(
        enablePadding = false
    ) {


    }
}