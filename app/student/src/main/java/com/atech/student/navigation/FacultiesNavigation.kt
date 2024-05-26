package com.atech.student.navigation

import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.student.ui.faculties.FacultiesViewModel
import com.atech.student.ui.faculties.compose.FacultiesScreen
import com.atech.ui_common.utils.fadeThroughComposable
import com.atech.ui_common.utils.sharedViewModel


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
        ) { entry ->
            val viewModel = entry.sharedViewModel<FacultiesViewModel>(navController)
            val states by viewModel.faculties.collectAsStateWithLifecycle(emptyList())
            FacultiesScreen(
                states = states
            )
        }
    }
}