package com.atech.research.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.research.ui.screens.login.screen.LogInScreen
import com.atech.ui_common.utils.animatedComposable


sealed class LogInScreenRoutes(
    val route: String
) {
    data object LogInScreen : LogInScreenRoutes("log_in_screen")
    data object VerifyScreen : LogInScreenRoutes("verify_screen")
}


fun NavGraphBuilder.logInScreenGraph(
    navHostController: NavHostController
) {
    navigation(
        route = ResearchHubNavigation.LogInScreen.route,
        startDestination = LogInScreenRoutes.LogInScreen.route
    ) {
        animatedComposable(
            route = LogInScreenRoutes.LogInScreen.route
        ) {
            LogInScreen(
                navHostController = navHostController
            )
        }
    }
}