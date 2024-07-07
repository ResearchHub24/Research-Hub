package com.atech.teacher.ui.research.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.ResearchModel
import com.atech.teacher.navigation.AddEditScreenArgs
import com.atech.teacher.navigation.ResearchRoutes
import com.atech.teacher.navigation.SendNotificationScreenArgs
import com.atech.teacher.navigation.ViewApplicationsArgs
import com.atech.teacher.navigation.fromResearchModel
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ResearchTeacherItem
import com.atech.ui_common.theme.ResearchHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResearchScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    state: List<ResearchModel> = emptyList(),
) {

    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(modifier = modifier,
        title = "Your Posted Research",
        scrollBehavior = topAppBarScrollBehavior,
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navHostController.navigate(AddEditScreenArgs(key = null))
                },
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                )
            }
        },
        actions = {
            IconButton(onClick = {
                navHostController.navigate(ResearchRoutes.AllChats.route)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Message,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }) { paddingValues ->
        if (state.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier.padding(paddingValues)
            )
            return@MainContainer
        }
        LazyColumn(
            modifier = Modifier.nestedScroll(topAppBarScrollBehavior.nestedScrollConnection),
            contentPadding = paddingValues
        ) {
            items(state) {
                ResearchTeacherItem(model = it, onClick = {
                    navHostController.navigate(
                        it.fromResearchModel()
                    )
                }, onViewAllApplication = {
                    navHostController.navigate(
                        ViewApplicationsArgs(
                            key = it.key ?: return@ResearchTeacherItem,
                            selectedUser = it.selectedUsers ?: ""
                        )
                    )
                }, onNotifyClick = {
                    navHostController.navigate(
                        SendNotificationScreenArgs(
                            key = it.key ?: return@ResearchTeacherItem,
                            title = it.title ?: "",
                            created = it.created ?: -1L
                        )
                    )
                })
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