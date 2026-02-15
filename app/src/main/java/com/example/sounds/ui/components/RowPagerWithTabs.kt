package com.example.sounds.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import com.example.sounds.ui.HomeScreenTabs
import com.example.sounds.ui.theme.colorKDB
import com.example.sounds.ui.theme.colorTelli
import kotlinx.coroutines.launch

@Composable
fun RowPagerWithTabs(
    modifier: Modifier = Modifier,
    tabs: List<String>,
    content: @Composable (Int) -> Unit
) {
    val screensPagerState = rememberPagerState( pageCount = { HomeScreenTabs.allTabs.size } )
    val coroutineScope = rememberCoroutineScope()


    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            val tabWidth = 90
            val tabRowWidth = tabWidth * HomeScreenTabs.allTabs.size
            TabRow (
                selectedTabIndex = screensPagerState.currentPage,
                containerColor = colorKDB,
                contentColor = colorTelli,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        modifier = Modifier.tabIndicatorOffset(
                            tabPositions[screensPagerState.currentPage]
                        ),
                        height = 2.dp,
                        color = colorTelli,
                    )
                },
                divider = {},
                modifier = Modifier
                    .width(tabRowWidth.dp)
                ,
            ) {
                HomeScreenTabs.allTabs.forEachIndexed { index, tab ->
                    Tab(
                        selected = screensPagerState.currentPage == index,
                        onClick = {
                            coroutineScope.launch {
                                screensPagerState.animateScrollToPage(index)
                            }
                        },
                        text = { Text(tab.title) },
                        modifier = Modifier
                            .width(tabWidth.dp)

                    )
                }
            }
        }
        HorizontalPager(
            state = screensPagerState,
            modifier = Modifier
                .fillMaxSize()
        ) { page ->
            content(page)
        }
    }
}
