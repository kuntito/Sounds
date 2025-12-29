package com.example.sounds.ui.components.song_playing

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

enum class RepeatBtnState(
    @get:DrawableRes val iconRes: Int,
) {
    NoRepeat(R.drawable.ic_repeat_off),
    RepeatOne(R.drawable.ic_repeat_one),
    RepeatAll(R.drawable.ic_repeat_all)
}

@Composable
fun RepeatBtn(
    modifier: Modifier = Modifier,
) {

    var state by remember { mutableStateOf(RepeatBtnState.NoRepeat) }
    val toggleState = {
        state = when(state) {
            RepeatBtnState.NoRepeat -> RepeatBtnState.RepeatOne
            RepeatBtnState.RepeatOne -> RepeatBtnState.RepeatAll
            RepeatBtnState.RepeatAll -> RepeatBtnState.NoRepeat
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
private fun RepeatBtnPreview() {
    PreviewColumn() {
        RepeatBtn()
    }
}