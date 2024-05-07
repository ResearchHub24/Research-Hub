package com.atech.research.ui.navigation

import androidx.compose.runtime.collectAsState
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.research.utils.fadeThroughComposable
import com.atech.student.ui.research.ResearchViewModel
import com.atech.student.ui.research.compose.ResearchScreen
import com.atech.ui_common.utils.sharedViewModel

sealed class ResearchScreenRoutes(
    val route: String,
) {
    data object ResearchScreen : ResearchScreenRoutes("research_screen")

}

fun NavGraphBuilder.researchScreenGraph(
    navController: NavHostController
) {
    navigation(
        route = MainScreenRoutes.Research.route,
        startDestination = ResearchScreenRoutes.ResearchScreen.route
    ) {
        fadeThroughComposable(
            route = ResearchScreenRoutes.ResearchScreen.route
        ) {
            val viewModel = it.sharedViewModel<ResearchViewModel>(navController = navController)
            val items = viewModel.research.collectAsState(initial = emptyList())
            ResearchScreen(
                items = items.value,
            )
        }
    }
}