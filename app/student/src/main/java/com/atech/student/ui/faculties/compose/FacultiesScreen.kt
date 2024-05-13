package com.atech.student.ui.faculties.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.atech.ui_common.R
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultiesScreen(modifier: Modifier = Modifier) {
    MainContainer(
        title = stringResource(id = R.string.faculties),
        modifier = modifier
    ) { paddingValues ->
//        if (items.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier
                    .padding(paddingValues),
                title = stringResource(id = R.string.no_faculties_found),
            )
            return@MainContainer
//        }
    }
}