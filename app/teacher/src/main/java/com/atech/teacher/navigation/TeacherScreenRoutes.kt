package com.atech.teacher.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.atech.teacher.ui.profile.compose.ProfileScreen
import com.atech.teacher.ui.research.ResearchViewModel
import com.atech.teacher.ui.research.compose.ResearchScreen
import com.atech.ui_common.utils.fadeThroughComposable
import com.atech.ui_common.utils.sharedViewModel

sealed class TeacherScreenRoutes(
    val route: String
) {
    data object ResearchScreen : TeacherScreenRoutes("research_screen")
    data object ProfileScreen : TeacherScreenRoutes("profile_screen")
}

@Composable
fun MainScreenTeacherNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: String = TeacherScreenRoutes.ResearchScreen.route,
    logOut: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination,
    ) {
        fadeThroughComposable(
            route = TeacherScreenRoutes.ResearchScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<ResearchViewModel>(navHostController)
            val research by viewModel.research.collectAsStateWithLifecycle(emptyList())
            ResearchScreen(
                state = research
            )
        }
        fadeThroughComposable(
            route = TeacherScreenRoutes.ProfileScreen.route
        ) {
            ProfileScreen(
                logOut = logOut
            )
        }
    }
}
