package com.atech.teacher.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ScreenSearchDesktop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.teacher.navigation.MainScreenTeacherNavigation
import com.atech.teacher.navigation.TeacherScreenRoutes
import com.atech.ui_common.R
import com.atech.ui_common.utils.NavBarModel
import com.atech.ui_common.utils.NavigationProvider

class TeacherNavigationProvider : NavigationProvider {
    override fun getVisibleScreens(): List<String> {
        return listOf(
            TeacherScreenRoutes.ResearchScreen.route,
            TeacherScreenRoutes.ProfileScreen.route
        )
    }

    override fun getNavigationItems(): List<NavBarModel> {
        return listOf(
            NavBarModel(
                title = R.string.research,
                selectedIcon = Icons.Rounded.ScreenSearchDesktop,
                route = TeacherScreenRoutes.ResearchScreen.route,
                destinationName = TeacherScreenRoutes.ResearchScreen.route,
            ),
            NavBarModel(
                title = R.string.profile,
                selectedIcon = Icons.Rounded.AccountCircle,
                route = TeacherScreenRoutes.ProfileScreen.route,
                destinationName = TeacherScreenRoutes.ProfileScreen.route
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