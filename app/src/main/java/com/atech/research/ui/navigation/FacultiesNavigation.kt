package com.atech.research.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.research.utils.fadeThroughComposable
import com.atech.student.ui.faculties.compose.FacultiesScreen


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
        startDestination =FacultiesScreenRoutes.FacultiesScreen.route
    ) {
        fadeThroughComposable(
            route = FacultiesScreenRoutes.FacultiesScreen.route
        ) {
            FacultiesScreen()
        }
    }
}