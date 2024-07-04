package com.atech.student.ui.faculties.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.atech.core.model.TeacherUserModel
import com.atech.core.retrofit.faculty.FacultyModel
import com.atech.ui_common.R
import com.atech.ui_common.common.BottomPadding
import com.atech.ui_common.common.FacultyItem
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.RegisterFacultyItem
import com.atech.ui_common.common.bottomPaddingLazy
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultiesScreen(
    modifier: Modifier = Modifier,
    states: List<FacultyModel> = emptyList(),
    registerTeacherState: List<TeacherUserModel> = emptyList()
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val tabItems = listOf(
        "All",
        "Registered Faculties"
    )
    MainContainer(
        title = stringResource(id = R.string.faculties),
        modifier = modifier,
        scrollBehavior = topAppBarScrollBehavior
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
        ) {
            var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }
            val pagerState = rememberPagerState {
                tabItems.size
            }
            LaunchedEffect(selectedTabIndex) {
                pagerState.animateScrollToPage(selectedTabIndex)
            }
            LaunchedEffect(pagerState.currentPage) {
                selectedTabIndex = pagerState.currentPage
            }
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabItems.forEachIndexed { index, s ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = {
                            selectedTabIndex = index
                        },
                        text = {
                            Text(s)
                        }
                    )
                }
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { index ->
                when (index) {
                    0 -> AllFaculties(
                        states = states,
                        topAppBarScrollBehavior = topAppBarScrollBehavior
                    )

                    1 -> RegisterFacultyScreen(
                        states = registerTeacherState,
                        topAppBarScrollBehavior = topAppBarScrollBehavior
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RegisterFacultyScreen(
    states: List<TeacherUserModel>,
    topAppBarScrollBehavior: TopAppBarScrollBehavior
) {
    if (states.isEmpty()) {
        GlobalEmptyScreen(
            modifier = Modifier,
            title = stringResource(id = R.string.no_faculties_found),
        )
        return
    }
    LazyColumn(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .fillMaxSize()
    ) {
        items(states) {
            RegisterFacultyItem(
                model = it,
                modifier = Modifier
            )
        }
        bottomPaddingLazy()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AllFaculties(
    states: List<FacultyModel>,
    topAppBarScrollBehavior: TopAppBarScrollBehavior
) {
    if (states.isEmpty()) {
        GlobalEmptyScreen(
            modifier = Modifier,
            title = stringResource(id = R.string.no_faculties_found),
        )
        return
    }
    Column(
        modifier = Modifier
            .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
    ) {
        states.forEach {
            FacultyItem(
                model = it, modifier = Modifier/*.animateItem(
                        fadeInSpec = spring(
                            dampingRatio = Spring.DampingRatioLowBouncy,
                            stiffness = Spring.StiffnessLow
                        )
                    )*/
            )
        }
        BottomPadding()
    }
}

@Preview
@Composable
private fun FacultyScreenPreview() {
    ResearchHubTheme {
        FacultiesScreen()
    }
}