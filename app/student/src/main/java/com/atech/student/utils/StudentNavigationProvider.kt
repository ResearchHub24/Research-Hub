package com.atech.student.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.Groups2
import androidx.compose.material.icons.rounded.ScreenSearchDesktop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.atech.student.navigation.FacultiesScreenRoutes
import com.atech.student.navigation.HomeScreenRoutes
import com.atech.student.navigation.MainScreenRoutes
import com.atech.student.navigation.ResearchScreenRoutes
import com.atech.student.navigation.facultiesScreenGraph
import com.atech.student.navigation.homeScreenGraph
import com.atech.student.navigation.researchScreenGraph
import com.atech.student.navigation.wishListScreenGraph
import com.atech.ui_common.R
import com.atech.ui_common.utils.NavBarModel
import com.atech.ui_common.utils.NavigationProvider


@Composable
fun MainScreenStudentNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: String = MainScreenRoutes.Home.route,
    navigateToLogIn: () -> Unit,
    logOut: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination,
    ) {
        homeScreenGraph(navController = navHostController, navigateToLogIn = navigateToLogIn)
        facultiesScreenGraph(navController = navHostController)
        researchScreenGraph(
            navController = navHostController,
            navigateToLogIn = navigateToLogIn,
            logOut = logOut
        )
        wishListScreenGraph(navController = navHostController)
    }
}


class StudentNavigationProvider : NavigationProvider {
    override fun getVisibleScreens(): List<String> =
        listOf(
            HomeScreenRoutes.HomeScreen.route,
            FacultiesScreenRoutes.FacultiesScreen.route,
            ResearchScreenRoutes.ResumeScreen.route,
            ResearchScreenRoutes.ResearchScreen.route
        )

    override fun getNavigationItems(): List<NavBarModel> =
        listOf(
            NavBarModel(
                title = R.string.home,
                selectedIcon = Icons.Rounded.Dashboard,
                route = MainScreenRoutes.Home.route,
                destinationName = HomeScreenRoutes.HomeScreen.route
            ),
            NavBarModel(
                title = R.string.research,
                selectedIcon = Icons.Rounded.ScreenSearchDesktop,
                route = MainScreenRoutes.Research.route,
                destinationName = ResearchScreenRoutes.ResearchScreen.route
            ),
            NavBarModel(
                title = R.string.faculties,
                selectedIcon = Icons.Rounded.Groups2,
                route = MainScreenRoutes.Faculties.route,
                destinationName = FacultiesScreenRoutes.FacultiesScreen.route
            ),
            NavBarModel(
                title = R.string.profile,
                selectedIcon = Icons.Rounded.AccountCircle,
                route = ResearchScreenRoutes.ResumeScreen.route,
                destinationName = ResearchScreenRoutes.ResumeScreen.route
            ),
        )

    @Composable
    override fun provideMainScreen(): @Composable (NavHostController, Modifier, () -> Unit, logOut: () -> Unit) -> Unit =
        { navController, modifier, login, logOut ->
            MainScreenStudentNavigation(
                navHostController = navController,
                modifier = modifier,
                navigateToLogIn = login,
                logOut = logOut
            )
        }
}