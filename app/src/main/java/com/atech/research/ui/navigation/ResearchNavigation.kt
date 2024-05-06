package com.atech.research.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.research.utils.fadeThroughComposable
import com.atech.student.ui.research.compose.ResearchScreen

sealed class ResearchScreenRoutes(
    val route: String,
) {
    data object ResearchScreen : ResearchScreenRoutes("research_screen")

}

fun NavGraphBuilder.researchScreenGraph(
    navController: NavHostController,
) {
    navigation(
        route = MainScreenRoutes.Research.route,
        startDestination = ResearchScreenRoutes.ResearchScreen.route
    ) {
        fadeThroughComposable(
            route = ResearchScreenRoutes.ResearchScreen.route
        ) {
            ResearchScreen()
        }
    }
}