package com.atech.teacher.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.atech.teacher.ui.profile.compose.ProfileScreen
import com.atech.ui_common.utils.fadeThroughComposable

sealed class ProfileRoutes(
    val routes: String
) {
    data object ProfileScreen : ProfileRoutes("profile_screen")
}

fun NavGraphBuilder.profileNavigation(
    navController: NavController,
    logOut: () -> Unit
) {
    navigation(
        startDestination = ProfileRoutes.ProfileScreen.routes,
        route = MainScreenRoutes.ProfileScreen.route
    ) {
        fadeThroughComposable(
            route = ProfileRoutes.ProfileScreen.routes
        ) {
            ProfileScreen(
                navController = navController,
                logOut = logOut
            )
        }
    }
}