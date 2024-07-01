package com.atech.teacher.ui.add.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.teacher.navigation.ViewMarkdownArgs
import com.atech.ui_common.R
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.MarkDown
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewMarkdown(
    modifier: Modifier = Modifier,
    args: ViewMarkdownArgs,
    navController: NavController = rememberNavController()
) {
    MainContainer(
        modifier = modifier,
        title = stringResource(R.string.view_markdown),
        onNavigationClick = {
            navController.navigateUp()
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state = rememberScrollState())
                .padding(MaterialTheme.spacing.medium)
                .padding(paddingValues)
        ) {
            MarkDown(
                modifier = Modifier
                    .fillMaxSize(),
                markDown = args.markdown
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewMarkdownPreview() {
    ResearchHubTheme {
        ViewMarkdown(
            args = ViewMarkdownArgs("markdown")
        )
    }
}