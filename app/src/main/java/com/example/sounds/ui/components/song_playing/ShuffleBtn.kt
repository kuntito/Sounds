package com.example.sounds.ui.components.song_playing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sounds.R
import com.example.sounds.ui.components.utils.AppIconButton
import com.example.sounds.ui.components.utils.PreviewColumn

@Composable
fun ShuffleBtn(
    modifier: Modifier = Modifier,
    isShuffled: Boolean,
    toggleShuffle: () -> Unit,
) {

    val iconRes  = if (isShuffled) R.drawable.ic_shuffle_on else R.drawable.ic_shuffle_off

    AppIconButton(
        iconRes = iconRes,
        modifier = modifier,
    ) {
        toggleShuffle()
    }
}

@Preview
@Composable
private fun ShuffleBtnPreview() {
    PreviewColumn() {
        var isShuffled by remember { mutableStateOf(false) }
        val toggleShuffle = {
            isShuffled = !isShuffled
        }
        ShuffleBtn(
            isShuffled = isShuffled,
            toggleShuffle = toggleShuffle
        )
    }
}