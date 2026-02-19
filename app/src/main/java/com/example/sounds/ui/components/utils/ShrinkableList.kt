package com.example.sounds.ui.components.utils

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.theme.colorTelli
import com.example.sounds.ui.theme.tsOrion
import kotlinx.coroutines.launch

/**
 * a list where items animate out when clicked.
 *
 * each item shrinks and fades on tap, and a snackbar briefly confirms the action.
 * the list is generic — it works with any item type, as long as a unique identifier
 * can be derived via [getId].
 *
 * the component only handles the visual removal. actual data changes (e.g. adding
 * a song to a playlist, dismissing a notification) are delegated to [onRemove].
 *
 * @param items the list of items to display.
 * @param getId returns a unique identifier for each item, used to track visibility.
 * @param onRemove called when an item is tapped — the caller handles the actual removal.
 * @param getDisplayMessageOnShrink returns the snackbar message shown after an item is removed.
 * @param topEdgePadding spacing above the first item.
 * @param bottomEdgePadding spacing below the last item.
 * @param itemComp composable that renders each item. should be non-clickable —
 *   click handling is managed internally.
 */
@Composable
fun <T, K> ShrinkableList(
    modifier: Modifier = Modifier,
    items: List<T>,
    getId: (T) -> K,
    onRemove: (T) -> Unit,
    topEdgePadding: Float,
    bottomEdgePadding: Float,
    getDisplayMessageOnShrink: (T) -> String,
    itemComp: @Composable (T) -> Unit,
) {

    var idsVisibleItems by remember { mutableStateOf(items.map(getId).toSet()) }

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        val animationDurationMillis = 500
        LazyColumn (
            modifier = Modifier
            ,
        ) {
            item {
                Spacer(
                    modifier = Modifier
                        .height(topEdgePadding.dp)
                )
            }
            items(items = items) { item ->
                val itemId = getId(item)
                AnimatedVisibility(
                    visible = itemId in idsVisibleItems,
                    exit = shrinkVertically(
                        animationSpec = tween(durationMillis = animationDurationMillis)
                    ) + fadeOut(
                        animationSpec = tween(durationMillis = animationDurationMillis)
                    )
                ) {
                    Box(
                        modifier = Modifier
                            .clickable {
                                idsVisibleItems -= itemId
                                onRemove(item)
                                scope.launch {
                                    snackBarHostState.currentSnackbarData?.dismiss()
                                    snackBarHostState.showSnackbar(
                                        message = getDisplayMessageOnShrink(item),
                                        duration = SnackbarDuration.Short,
                                    )
                                }
                            }
                    ) {
                        itemComp(item)
                    }
                }
            }
            item {
                Spacer(
                    modifier = Modifier
                        .height(bottomEdgePadding.dp)
                )
            }
        }
        SnackbarHost(
            hostState = snackBarHostState,
            snackbar = { data ->
                AppSnackBar(
                    message = data.visuals.message
                )
            },
            modifier = Modifier.align(
                Alignment.TopCenter
            )
        )
    }
}


@Preview
@Composable
private fun ShrinkableListPreview() {
    PreviewColumn {
        val itemList = (1..10).map { "item $it" }
        ShrinkableList<String, String>(
            items = itemList,
            getId = { it },
            onRemove = {},
            itemComp = { text -> ItemSample(text = text) },
            topEdgePadding = 0f,
            bottomEdgePadding = 0f,
            getDisplayMessageOnShrink = { "item handled" }
        )
    }
}

@Composable
fun ItemSample(
    text: String,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .border(width = 1.dp, color = colorTelli)
            .fillMaxWidth()
            .height(48.dp)
        ,
    ) {
        Text(
            text = text,
            color = colorTelli,
            style = tsOrion,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}