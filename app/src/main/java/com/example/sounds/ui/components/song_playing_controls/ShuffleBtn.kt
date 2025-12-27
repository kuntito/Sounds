package com.example.sounds.ui.components.song_playing_controls

import androidx.annotation.DrawableRes
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

enum class ShuffleBtnState(
    @get:DrawableRes val iconRes: Int,
) {
    NoShuffle(R.drawable.ic_shuffle_off),
    ShuffleOn(R.drawable.ic_shuffle_on)
}

@Composable
fun ShuffleBtn(
    modifier: Modifier = Modifier,
) {
    var state by remember { mutableStateOf(ShuffleBtnState.NoShuffle) }
    val toggleState = {
        state = when(state) {
            ShuffleBtnState.NoShuffle -> ShuffleBtnState.ShuffleOn
            ShuffleBtnState.ShuffleOn -> ShuffleBtnState.NoShuffle
        }
    }

    AppIconButton(
        iconRes = state.iconRes,
        modifier = modifier,
    ) {
        toggleState()
    }
}

@Preview
@Composable
private fun ShuffleBtnPreview() {
    PreviewColumn() {
        ShuffleBtn()
    }
}