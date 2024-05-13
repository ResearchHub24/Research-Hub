package com.atech.student.navigation

enum class RouteName(val value: String) {
    HOME("home"),
    FACULTIES("faculties"),
    RESEARCH("research"),
    WISHLIST("wishlist"),
}

sealed class MainScreenRoutes(
    val route: String,
) {
    data object Home : MainScreenRoutes(RouteName.HOME.value)
    data object Faculties : HomeScreenRoutes(RouteName.FACULTIES.value)
    data object Research : HomeScreenRoutes(RouteName.RESEARCH.value)
    data object Wishlist : HomeScreenRoutes(RouteName.WISHLIST.value)
}
