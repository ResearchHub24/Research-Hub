package com.atech.teacher.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ScreenSearchDesktop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.atech.teacher.navigation.MainScreenRoutes
import com.atech.teacher.navigation.ProfileRoutes
import com.atech.teacher.navigation.ResearchRoutes
import com.atech.teacher.navigation.profileNavigation
import com.atech.teacher.navigation.researchScreenGraph
import com.atech.ui_common.R
import com.atech.ui_common.utils.NavBarModel
import com.atech.ui_common.utils.NavigationProvider


@Composable
fun MainScreenTeacherNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: String = MainScreenRoutes.ResearchScreen.route,
    logOut: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination,
    ) {
        researchScreenGraph(navHostController)
        profileNavigation(
            navController = navHostController,
            logOut = logOut
        )
    }
}

class TeacherNavigationProvider : NavigationProvider {
    override fun getVisibleScreens(): List<String> {
        return listOf(
            ResearchRoutes.ResearchScreen.route,
            ProfileRoutes.ProfileScreen.routes
        )
    }

    override fun getNavigationItems(): List<NavBarModel> {
        return listOf(
            NavBarModel(
                title = R.string.research,
                selectedIcon = Icons.Rounded.ScreenSearchDesktop,
                route = MainScreenRoutes.ResearchScreen.route,
                destinationName = MainScreenRoutes.ResearchScreen.route,
            ),
            NavBarModel(
                title = R.string.profile,
                selectedIcon = Icons.Rounded.AccountCircle,
                route = MainScreenRoutes.ProfileScreen.route,
                destinationName = MainScreenRoutes.ProfileScreen.route
            )
        )
    }

    @Composable
    override fun provideMainScreen(): @Composable (navController: NavHostController, modifier: Modifier, navigateToLogIn: () -> Unit, logOut: () -> Unit) -> Unit =
        { navController, modifier, _, logOut ->
            MainScreenTeacherNavigation(
                navHostController = navController,
                modifier = modifier,
                logOut = logOut
            )
        }
}