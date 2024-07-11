package com.atech.student.ui.research.main.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Message
import androidx.compose.material.icons.rounded.Bookmarks
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.ResearchModel
import com.atech.core.utils.DataState
import com.atech.student.navigation.MainScreenRoutes
import com.atech.student.navigation.ResearchScreenRoutes
import com.atech.student.ui.research.main.ResearchScreenEvents
import com.atech.student.ui.resume.ResumeScreenEvents
import com.atech.ui_common.R
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ProgressBar
import com.atech.ui_common.common.ResearchItem
import com.atech.ui_common.common.bottomPaddingLazy
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResearchScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    items: DataState<List<ResearchModel>> = DataState.Loading,
    onEvent: (ResearchScreenEvents) -> Unit = {},
    resumeEvents: (ResumeScreenEvents) -> Unit = {}
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    LaunchedEffect(lifecycleState) {
        when (lifecycleState) {
            Lifecycle.State.RESUMED -> {
                resumeEvents(ResumeScreenEvents.UpdateUserDetails)
                if (items is DataState.Success) onEvent.invoke(
                    ResearchScreenEvents.DeleteResearchNotInKeys(
                        items.data.map {
                            it.key ?: ""
                        })
                )
            }

            else -> {}
        }
    }
    LaunchedEffect(
        items
    ) {
        if (items is DataState.Success)
            onEvent.invoke(ResearchScreenEvents.DeleteResearchNotInKeys(items.data.map {
                it.key ?: ""
            }))
    }
    MainContainer(title = stringResource(id = R.string.research),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        actions = {
            IconButton(onClick = {
                navController.navigate(ResearchScreenRoutes.AllChats.route)
            }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.Message,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = {
                navController.navigate(
                    MainScreenRoutes.Wishlist.route
                )
            }) {
                Icon(
                    imageVector = Icons.Rounded.Bookmarks,
                    contentDescription = stringResource(R.string.wishlist),
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            IconButton(onClick = {
//                navController.navigate(
//                    MainScreenRoutes.Wishlist.route
//                )
            }) {
                Icon(
                    imageVector = Icons.Rounded.FilterAlt,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }) { paddingValues ->
        when (items) {
            is DataState.Error -> {}
            DataState.Loading -> {
                ProgressBar(paddingValues)
            }

            is DataState.Success -> {
                if (items.data.isEmpty()) {
                    GlobalEmptyScreen(
                        modifier = Modifier.padding(paddingValues),
                        title = stringResource(id = R.string.no_research_found),
                    )
                    return@MainContainer
                }
                LazyColumn(
                    contentPadding = paddingValues,
                    modifier = Modifier,
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
                ) {
                    items(items.data.size) { index ->
                        ResearchItem(model = items.data[index], onClick = {
                            onEvent.invoke(ResearchScreenEvents.OnItemClick(items.data[index]))
                            navController.navigate(ResearchScreenRoutes.DetailScreen.route)
                        })
                    }
                    bottomPaddingLazy()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ResearchScreenPreview() {
    ResearchHubTheme {
        ResearchScreen()
    }
}