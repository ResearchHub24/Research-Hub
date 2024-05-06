package com.atech.research.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.research.utils.fadeThroughComposable
import com.atech.student.ui.home.compose.HomeScreen

sealed class HomeScreenRoutes(
    val route: String,
) {
    data object HomeScreen : HomeScreenRoutes("home_screen")

}

fun NavGraphBuilder.homeScreenGraph(
    navController: NavHostController,
) {
    navigation(
        route = MainScreenRoutes.Home.route,
        startDestination = HomeScreenRoutes.HomeScreen.route
    ) {
        fadeThroughComposable(
            route = HomeScreenRoutes.HomeScreen.route
        ) {
            HomeScreen()
        }
    }
}