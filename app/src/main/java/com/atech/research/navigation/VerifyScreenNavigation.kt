package com.atech.research.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.research.ui.screens.verify.VerifyViewModel
import com.atech.research.ui.screens.verify.compose.VerifyScreen
import com.atech.ui_common.utils.animatedComposable
import com.atech.ui_common.utils.sharedViewModel

sealed class VerifyScreenRoute {
    data object VerifyScreen : LogInScreenRoutes("verify_screen")
}

fun NavGraphBuilder.verifyScreenGraph(
    navHostController: NavHostController
) {
    navigation(
        route = ResearchHubNavigation.VerifyScreen.route,
        startDestination = VerifyScreenRoute.VerifyScreen.route
    ){
        animatedComposable(
            route = VerifyScreenRoute.VerifyScreen.route,
        ) { entry ->
            val viewModel =
                entry.sharedViewModel<VerifyViewModel>(navController = navHostController)
            VerifyScreen(

            )
        }
    }
}