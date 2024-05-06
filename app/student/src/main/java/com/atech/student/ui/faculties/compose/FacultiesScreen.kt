package com.atech.student.ui.faculties.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.atech.ui_common.R
import com.atech.ui_common.common.MainContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultiesScreen(modifier: Modifier = Modifier) {
    MainContainer(
        title = stringResource(id = R.string.faculties),
        modifier = modifier
    ) { paddingValues ->

    }
}