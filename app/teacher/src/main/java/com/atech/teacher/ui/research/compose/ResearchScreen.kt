package com.atech.teacher.ui.research.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.atech.core.model.ResearchModel
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ResearchTeacherItem
import com.atech.ui_common.theme.ResearchHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResearchScreen(
    modifier: Modifier = Modifier,
    state: List<ResearchModel> = emptyList()
) {
    MainContainer(
        modifier = modifier,
        title = "All Research",
        floatingActionButton = {
            FloatingActionButton(
                onClick = {}
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null
                )
            }
        }
    ) { paddingValues ->
        if (state.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier.padding(paddingValues)
            )
            return@MainContainer
        }
        LazyColumn (
            modifier = Modifier,
            contentPadding = paddingValues
        ){
            items(state) {
                ResearchTeacherItem(
                    model = it
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ResearchScreenPreview() {
    ResearchHubTheme {
        ResearchScreen()
    }
}