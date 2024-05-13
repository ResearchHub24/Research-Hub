package com.atech.student.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.student.ui.research.detail.compose.ResearchDetailScreen
import com.atech.student.ui.research.main.ResearchViewModel
import com.atech.student.ui.research.main.compose.ResearchScreen
import com.atech.ui_common.utils.fadeThroughComposable
import com.atech.ui_common.utils.sharedViewModel

sealed class ResearchScreenRoutes(
    val route: String,
) {
    data object ResearchScreen : ResearchScreenRoutes("research_screen")
    data object DetailScreen : ResearchScreenRoutes("detail_screen")
}

fun NavGraphBuilder.researchScreenGraph(
    navController: NavHostController
) {
    navigation(
        route = RouteName.RESEARCH.value,
        startDestination = ResearchScreenRoutes.ResearchScreen.route
    ) {
        fadeThroughComposable(
            route = ResearchScreenRoutes.ResearchScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResearchViewModel>(navController = navController)
            val items = viewModel.research.collectAsState(initial = emptyList())
            ResearchScreen(
                items = items.value,
                navController = navController,
                onEvent = viewModel::onEvent
            )
        }

        fadeThroughComposable(
            route = ResearchScreenRoutes.DetailScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResearchViewModel>(navController = navController)
            val clickedItem = viewModel.clickItem.value
            ResearchDetailScreen(
                events = viewModel::onEvent,
                navController = navController,
                model = clickedItem ?: return@fadeThroughComposable
            )
        }
    }
}