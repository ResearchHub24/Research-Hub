package com.atech.student.ui.resume.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.ResearchModel
import com.atech.ui_common.R
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ResearchItem
import com.atech.ui_common.common.bottomPaddingLazy
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllApplicationScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    state: List<Pair<ResearchModel, Boolean>> = emptyList()
) {
    MainContainer(modifier = modifier, title = "Your Application", onNavigationClick = {
        navHostController.popBackStack()
    }) { paddingValues ->
        if (state.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier
                    .padding(paddingValues),
                title = stringResource(id = R.string.no_application_found),
            )
            return@MainContainer
        }
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(state.size) { index ->
                ResearchItem(
                    model = state[index].first,
                    onClick = {

                    },
                    isSelected = state[index].second,
                    canShowViewDetailButton = false
                )
            }
            bottomPaddingLazy()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AppApplicationScreenPreview() {
    ResearchHubTheme {
        AllApplicationScreen()
    }
}