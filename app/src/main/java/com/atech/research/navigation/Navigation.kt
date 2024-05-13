package com.atech.research.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.atech.research.ui.screens.main.MainScreen
import com.atech.student.navigation.MainScreenRoutes
import com.atech.student.navigation.facultiesScreenGraph
import com.atech.student.navigation.homeScreenGraph
import com.atech.student.navigation.researchScreenGraph
import com.atech.student.navigation.wishListScreenGraph
import com.atech.ui_common.utils.animatedComposable

enum class TopLevelRoutes(
    val route: String,
) {
    HOME("home"),
    LOGIN("login")
}

sealed class ResearchHubNavigation(
    val route: String
) {
    data object MainScreen : ResearchHubNavigation(TopLevelRoutes.HOME.route)
    data object LogInScreen : ResearchHubNavigation(TopLevelRoutes.LOGIN.route)
}

@Composable
fun ResearchHubNavigation(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    startDestination: ResearchHubNavigation = ResearchHubNavigation.MainScreen
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        animatedComposable(
            route = ResearchHubNavigation.MainScreen.route
        ) {
            MainScreen()
        }
        logInScreenGraph(navController)
    }
}


@Composable
fun MainScreenStudentNavigation(
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
        facultiesScreenGraph(navController = navHostController)
        researchScreenGraph(navController = navHostController)
        wishListScreenGraph(navController = navHostController)
    }
}
