package com.atech.student.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.student.ui.faculties.compose.FacultiesScreen
import com.atech.ui_common.utils.fadeThroughComposable


sealed class FacultiesScreenRoutes(
    val route: String,
) {
    data object FacultiesScreen : FacultiesScreenRoutes("faculties_screen")

}

fun NavGraphBuilder.facultiesScreenGraph(
    navController: NavHostController,
) {
    navigation(
        route = MainScreenRoutes.Faculties.route,
        startDestination = FacultiesScreenRoutes.FacultiesScreen.route
    ) {
        fadeThroughComposable(
            route = FacultiesScreenRoutes.FacultiesScreen.route
        ) {
            FacultiesScreen()
        }
    }
}