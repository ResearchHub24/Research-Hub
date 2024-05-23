package com.atech.research.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.atech.research.ui.screens.main.MainScreen
import com.atech.ui_common.utils.NavBarModel
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
    visibleScreens: List<String> = emptyList(),
    navigationItem: List<NavBarModel> = emptyList(),
    startDestination: ResearchHubNavigation = ResearchHubNavigation.MainScreen,
    mainScreen: @Composable (navController: NavHostController, modifier: Modifier, navigateToLogIn: () -> Unit) -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        animatedComposable(
            route = ResearchHubNavigation.MainScreen.route
        ) {
            MainScreen(
                visibleScreens = visibleScreens,
                navigationItem = navigationItem,
                mainScreen = mainScreen,
                mainNavHost = navController
            )
        }
        logInScreenGraph(navController)
    }
}


