package com.atech.teacher.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.ScreenSearchDesktop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.atech.teacher.navigation.MainScreenRoutes
import com.atech.teacher.navigation.ProfileRoutes
import com.atech.teacher.navigation.ResearchRoutes
import com.atech.teacher.navigation.profileNavigation
import com.atech.teacher.navigation.researchScreenGraph
import com.atech.teacher.ui.verify.VerifyScreenViewModel
import com.atech.teacher.ui.verify.compose.VerifyScreen
import com.atech.ui_common.R
import com.atech.ui_common.utils.NavBarModel
import com.atech.ui_common.utils.NavigationProvider
import com.atech.ui_common.utils.animatedComposable


@Composable
fun MainScreenTeacherNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: String = MainScreenRoutes.VerifyScreen.route,
    logOut: () -> Unit
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination,
    ) {
        animatedComposable(
            route = MainScreenRoutes.VerifyScreen.route
        ) {
            val viewModel: VerifyScreenViewModel = hiltViewModel()
            val verifyScreenState by viewModel.verifyScreenState
            VerifyScreen(
                verifyScreen = verifyScreenState,
                onVerifyScreenEvent = viewModel::onVerifyScreenEvent,
                navController = navHostController
            )
        }
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
                route = ResearchRoutes.ResearchScreen.route,
                destinationName = ResearchRoutes.ResearchScreen.route,
            ),
            NavBarModel(
                title = R.string.profile,
                selectedIcon = Icons.Rounded.AccountCircle,
                route = ProfileRoutes.ProfileScreen.routes,
                destinationName = ProfileRoutes.ProfileScreen.routes
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