package com.atech.student.navigation

import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.student.ui.home.HomeViewModel
import com.atech.student.ui.home.compose.HomeScreen
import com.atech.ui_common.utils.fadeThroughComposable
import com.atech.ui_common.utils.sharedViewModel

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
        ) { entry ->
            val viewModel = entry.sharedViewModel<HomeViewModel>(navController = navController)
            val userModel by viewModel.userModel
            HomeScreen(
                model = userModel,
                navController = navController
            )
        }
    }
}