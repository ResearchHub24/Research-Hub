package com.atech.teacher.ui.view_applications.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.ResearchPublishModel
import com.atech.teacher.navigation.StudentProfileArgs
import com.atech.teacher.navigation.ViewApplicationsArgs
import com.atech.teacher.ui.view_applications.ViewApplicationEvents
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.toast
import com.atech.ui_common.theme.ResearchHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ViewApplicationScreen(
    modifier: Modifier = Modifier,
    args: ViewApplicationsArgs,
    submittedForms: List<ResearchPublishModel> = emptyList(),
    navController: NavController = rememberNavController(),
    onEvent: (ViewApplicationEvents) -> Unit = {}
) {
    val context = LocalContext.current
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    LifecycleEventEffect(Lifecycle.Event.ON_START) {
        if (args.key.isNotEmpty()) onEvent.invoke(ViewApplicationEvents.SetKeyFromArgs(args.key))
        else {
            toast(context, "Invalid key")
            navController.navigateUp()
        }
    }
    MainContainer(
        modifier = modifier,
        scrollBehavior = topAppBarScrollBehavior,
        title = "View Applications",
        onNavigationClick = {
            navController.navigateUp()
        },
    ) { paddingValues ->
        if (submittedForms.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier.padding(paddingValues), title = "No Forms Submitted Yet"
            )
            return@MainContainer
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues
        ) {
            items(submittedForms) {
                ApplicationItem(
                    model = it,
                    onViewProfileClick = {
                        navController.navigate(
                            StudentProfileArgs(
                                uid = it.uid ?: return@ApplicationItem
                            )
                        )
                    }
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ViewApplicationScreenPreview() {
    ResearchHubTheme {
        ViewApplicationScreen(
            args = ViewApplicationsArgs("Temp Key")
        )
    }
}