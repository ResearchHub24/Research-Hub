package com.atech.student.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Checklist
import androidx.compose.material.icons.rounded.Dashboard
import androidx.compose.material.icons.rounded.PersonPinCircle
import androidx.compose.material.icons.rounded.ScreenSearchDesktop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.atech.ui_common.R
import com.atech.ui_common.utils.NavBarModel
import com.atech.ui_common.utils.NavigationProvider


@Composable
fun MainScreenStudentNavigation(
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    startDestination: String = MainScreenRoutes.Home.route
) {
    NavHost(
        modifier = modifier,
        navController = navHostController,
        startDestination = startDestination,
    ) {
        homeScreenGraph(navController = navHostController)
        facultiesScreenGraph(navController = navHostController)
        researchScreenGraph(navController = navHostController)
        wishListScreenGraph(navController = navHostController)
    }
}


class StudentNavigationProvider : NavigationProvider {
    override fun getVisibleScreens(): List<String> =
        listOf(
            HomeScreenRoutes.HomeScreen.route,
            FacultiesScreenRoutes.FacultiesScreen.route,
            WishlistScreenRoutes.WishListScreen.route,
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
                selectedIcon = Icons.Rounded.PersonPinCircle,
                route = MainScreenRoutes.Faculties.route,
                destinationName = FacultiesScreenRoutes.FacultiesScreen.route
            ),
            NavBarModel(
                title = R.string.wishlist,
                selectedIcon = Icons.Rounded.Checklist,
                route = MainScreenRoutes.Wishlist.route,
                destinationName = WishlistScreenRoutes.WishListScreen.route
            ),
        )

    @Composable
    override fun provideMainScreen(): @Composable (navController: NavHostController, modifier: Modifier) -> Unit =
        { navController, modifier ->
            MainScreenStudentNavigation(
                navHostController = navController,
                modifier = modifier
            )
        }
}