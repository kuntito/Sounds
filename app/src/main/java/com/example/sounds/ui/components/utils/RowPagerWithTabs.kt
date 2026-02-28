package com.example.sounds.ui.components.utils

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.sounds.ui.theme.colorKDB
import com.example.sounds.ui.theme.colorTelli
import com.example.sounds.ui.theme.tsOrion
import kotlinx.coroutines.launch

@Composable
fun RowPagerWithTabs(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    pagerState: PagerState,
    content: @Composable (Int) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val tabWidth = 100
            val tabRowWidth = tabWidth * tabs.size
            TabRow (
                selectedTabIndex = pagerState.currentPage,
                containerColor = colorKDB,
                contentColor = colorTelli,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[pagerState.currentPage]
                        ),
                        height = 2.dp,
                        color = colorTelli,
                    )
                },
                divider = {},
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .width(tabRowWidth.dp)
                ,
            ) {
                tabs.forEachIndexed { index, tabTitle ->
                    Tab(
                        selected = pagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(index)
                            }
                        },
                        text = {
                            Text(
                                text = tabTitle,
                                style = tsOrion,
                            )
                        },
                        modifier = Modifier
                            .width(tabWidth.dp)

                    )
                }
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            content(page)
        }
    }
}
