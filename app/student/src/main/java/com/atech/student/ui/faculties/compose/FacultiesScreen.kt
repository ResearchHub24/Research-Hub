package com.atech.student.ui.faculties.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.atech.core.retrofit.FacultyModel
import com.atech.ui_common.R
import com.atech.ui_common.common.BottomPadding
import com.atech.ui_common.common.FacultyItem
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun FacultiesScreen(
    modifier: Modifier = Modifier, states: List<FacultyModel> = emptyList()
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(
        title = stringResource(id = R.string.faculties),
        modifier = modifier,
        scrollBehavior = topAppBarScrollBehavior
    ) { paddingValues ->
        if (states.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier.padding(paddingValues),
                title = stringResource(id = R.string.no_faculties_found),
            )
            return@MainContainer
        }
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .verticalScroll(rememberScrollState()),
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
}