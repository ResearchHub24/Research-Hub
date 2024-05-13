package com.atech.student.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.navigation
import com.atech.student.ui.wishlist.compose.WishlistScreen
import com.atech.ui_common.utils.fadeThroughComposable


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