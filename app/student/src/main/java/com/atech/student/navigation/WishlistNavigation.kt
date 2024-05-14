package com.atech.student.navigation

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.student.ui.research.main.ResearchViewModel
import com.atech.student.ui.wishlist.WishListViewModel
import com.atech.student.ui.wishlist.compose.WishlistScreen
import com.atech.ui_common.utils.fadeThroughComposable
import com.atech.ui_common.utils.sharedViewModel


sealed class WishlistScreenRoutes(
    val route: String,
) {
    data object WishListScreen : HomeScreenRoutes("wishlist_screen")

}

fun NavGraphBuilder.wishListScreenGraph(
    navController: NavHostController,
) {
    navigation(
        route = MainScreenRoutes.Wishlist.route,
        startDestination = WishlistScreenRoutes.WishListScreen.route
    ) {
        fadeThroughComposable(
            route = WishlistScreenRoutes.WishListScreen.route
        ) { entry ->
            val viewModel = entry.sharedViewModel<WishListViewModel>(navController = navController)
            val items by viewModel.getAllResearch.collectAsState(initial = emptyList())
            WishlistScreen(
                items = items,
                navController = navController,
            )
        }
    }
}