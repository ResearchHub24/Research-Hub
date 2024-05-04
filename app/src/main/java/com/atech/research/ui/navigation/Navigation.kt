package com.atech.research.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.navigation
import com.atech.research.ui.screens.home.compose.HomeScreen
import com.atech.research.ui.screens.main.MainScreen
import com.atech.research.utils.animatedComposable
import com.atech.research.utils.fadeThroughComposable

enum class TopLevelRoutes(
    val route: String,
) {
    HOME("home"),
}

sealed class ResearchHubNavigation(
    val route: String
) {
    data object MainScreen : ResearchHubNavigation(TopLevelRoutes.HOME.route)
}

@Composable
fun ResearchHubNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    NavHost(
        navController = navController,
        startDestination = ResearchHubNavigation.MainScreen.route,
        modifier = modifier
    ) {
        animatedComposable(
            route = ResearchHubNavigation.MainScreen.route
        ) {
            MainScreen()
        }
    }
}


sealed class MainScreenRoutes(
    val route: String,
) {
    data object Home : MainScreenRoutes("home")
}


@Composable
fun MainScreenNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: String = MainScreenRoutes.Home.route
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination,
    ) {
        homeScreenGraph(navController = navHostController)
    }
}

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