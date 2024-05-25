package com.atech.student.ui.faculties.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.atech.core.retrofit.FacultyModel
import com.atech.ui_common.R
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.TeacherItem
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultiesScreen(
    modifier: Modifier = Modifier,
    states: List<FacultyModel> = emptyList()
) {
    MainContainer(
        title = stringResource(id = R.string.faculties),
        modifier = modifier,
    ) { paddingValues ->
        if (states.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier
                    .padding(paddingValues),
                title = stringResource(id = R.string.no_faculties_found),
            )
            return@MainContainer
        }
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(states) {
                TeacherItem(
                    model = it
                )
            }
        }
    }
}