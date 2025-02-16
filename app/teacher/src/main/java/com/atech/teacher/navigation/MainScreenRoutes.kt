package com.atech.teacher.navigation


enum class RouteName(val value: String) {
    RESEARCH("research"),
    PROFILE("Profile"),
    VERIFY("Verify")
}

sealed class MainScreenRoutes(
    val route: String
) {
    data object ResearchScreen : MainScreenRoutes(RouteName.RESEARCH.value)
    data object ProfileScreen : MainScreenRoutes(RouteName.PROFILE.value)
    data object VerifyScreen : MainScreenRoutes(RouteName.VERIFY.name)
}

