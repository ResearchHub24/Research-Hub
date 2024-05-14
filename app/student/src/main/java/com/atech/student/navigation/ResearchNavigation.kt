package com.atech.student.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.navigation
import com.atech.student.ui.research.detail.compose.ResearchDetailScreen
import com.atech.student.ui.research.main.ResearchScreenEvents
import com.atech.student.ui.research.main.ResearchViewModel
import com.atech.student.ui.research.main.compose.ResearchScreen
import com.atech.ui_common.utils.animatedComposable
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

        animatedComposable(
            route = ResearchScreenRoutes.DetailScreen.route + "?key={key}",
            arguments = listOf(
                navArgument("key") {
                    type = NavType.StringType
                    defaultValue = null
                    nullable = true
                }
            )
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResearchViewModel>(navController = navController)
            entry.arguments?.getString("key")?.let {
                viewModel.onEvent(ResearchScreenEvents.SetDataFromArgs(it))
            }
            val clickedItem by viewModel.clickItem
            val isExistInWishList by viewModel.isExist
            val isFromArgs by viewModel.isFromArgs
            ResearchDetailScreen(
                onEvent = viewModel::onEvent,
                navController = navController,
                model = clickedItem ?: return@animatedComposable,
                isExistInWishList = isExistInWishList,
                isFromArgs = isFromArgs
            )
        }
    }
}