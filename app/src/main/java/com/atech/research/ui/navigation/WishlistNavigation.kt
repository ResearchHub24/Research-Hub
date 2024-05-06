package com.atech.research.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.research.utils.fadeThroughComposable
import com.atech.student.ui.wishlist.compose.WishlistScreen


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
        ) {
            WishlistScreen()
        }
    }
}