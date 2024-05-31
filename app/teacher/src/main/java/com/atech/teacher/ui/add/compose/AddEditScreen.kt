package com.atech.teacher.ui.add.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.teacher.navigation.AddEditScreenArgs
import com.atech.teacher.navigation.replaceNA
import com.atech.teacher.ui.add.AddEditScreenEvent
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    args: AddEditScreenArgs,
    state: AddEditScreenArgs,
    onEvent: (AddEditScreenEvent) -> Unit = {}
) {
    LaunchedEffect(args) {
        onEvent.invoke(AddEditScreenEvent.SetArgs(args))
    }
    MainContainer(modifier = modifier,
        title = if (state.key == null) "Add Research" else "Edit Research",
        onNavigationClick = {
            navHostController.popBackStack()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
        ) {
            EditText(modifier = Modifier.fillMaxWidth(),
                value = state.title,
                placeholder = "Title",
                supportingMessage = "Title of the research",
                onValueChange = { value ->
                    onEvent.invoke(AddEditScreenEvent.OnTitleChange(value))
                })
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditScreenPreview() {
    ResearchHubTheme {
        AddEditScreen(
            state = AddEditScreenArgs(key = null).replaceNA(),
            args = AddEditScreenArgs(key = null).replaceNA()
        )
    }
}