package com.atech.teacher.ui.view_applications.compose

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.Action
import com.atech.core.model.ResearchPublishModel
import com.atech.core.utils.fromJsonList
import com.atech.teacher.navigation.StudentProfileArgs
import com.atech.teacher.navigation.ViewApplicationsArgs
import com.atech.teacher.ui.view_applications.ViewApplicationEvents
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.toast
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
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
    var selectedUser by remember { mutableStateOf(fromJsonList<String>(args.selectedUser)) }
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
        val filteredSelectedUsers = submittedForms.filter { selectedUser.contains(it.uid) }
        val map = mapOf(Action.SELECTED.name.lowercase()
            .replaceFirstChar { it.uppercaseChar() } to filteredSelectedUsers,
            (if (filteredSelectedUsers.isEmpty()) "Application" else Action.UNSELECTED.name.lowercase()
                .replaceFirstChar { it.uppercaseChar() }) to submittedForms.filter {
                !selectedUser.contains(
                    it.uid
                )
            })
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues
        ) {
            map.forEach { (initial, forms) ->
                if (forms.isNotEmpty()) stickyHeader {
                    Text(
                        modifier = Modifier.padding(start = MaterialTheme.spacing.medium),
                        text = initial,
                        style = MaterialTheme.typography.labelSmall
                    )
                }
                items(forms) { model ->
                    ApplicationItem(model = model,
                        action = if (initial == Action.SELECTED.name.lowercase()
                                .replaceFirstChar { it.uppercaseChar() }
                        ) Action.SELECTED else Action.UNSELECTED,
                        onViewProfileClick = {
                            navController.navigate(
                                StudentProfileArgs(
                                    uid = model.uid ?: return@ApplicationItem
                                )
                            )
                        },
                        onActionClick = { action ->
                            if (action == if (initial == Action.SELECTED.name.lowercase()
                                        .replaceFirstChar { it.uppercaseChar() }
                                ) Action.SELECTED else Action.UNSELECTED
                            ) {
                                return@ApplicationItem // Save Item click
                            }
                            onEvent.invoke(
                                ViewApplicationEvents.SelectUserAction(action = action,
                                    ui = model.uid!!,
                                    name = model.studentName ?: "",
                                    profileUrl = model.profileImg!!,
                                    onComplete = {
                                        if (it != null) {
                                            toast(context, it)
                                            return@SelectUserAction
                                        }
                                        toast(
                                            context, "User ${action.name.lowercase()} successfully"
                                        )
                                        selectedUser =
                                            if (action == Action.SELECTED) selectedUser.toMutableList()
                                                .apply { add(model.uid!!) }
                                            else selectedUser.toMutableList()
                                                .apply { remove(model.uid!!) }
                                    })
                            )
                        })
                }
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