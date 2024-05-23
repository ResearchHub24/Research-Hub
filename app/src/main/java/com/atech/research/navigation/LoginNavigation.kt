package com.atech.research.navigation

import android.content.Intent
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.atech.research.ui.screens.login.LoginViewModel
import com.atech.research.ui.screens.login.screen.LogInScreen
import com.atech.ui_common.utils.DeepLinkRoutes
import com.atech.ui_common.utils.animatedComposable
import com.atech.ui_common.utils.sharedViewModel


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
            route = LogInScreenRoutes.LogInScreen.route,
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = DeepLinkRoutes.LOGIN.route
                    action = Intent.ACTION_VIEW
                }
            )
        ) { entry ->
            val viewModel = entry.sharedViewModel<LoginViewModel>(navController = navHostController)
            val logInState = viewModel.logInState.value
            LogInScreen(
                navHostController = navHostController,
                logInState = logInState,
                onEvent = viewModel::onEvent
            )
        }
    }
}