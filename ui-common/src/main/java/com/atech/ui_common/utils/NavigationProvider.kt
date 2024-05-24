package com.atech.ui_common.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController

interface NavigationProvider {
    fun getVisibleScreens(): List<String>
    fun getNavigationItems(): List<NavBarModel>

    @Composable
    fun provideMainScreen(): @Composable (
        navController: NavHostController,
        modifier: Modifier,
        navigateToLogIn: () -> Unit,
        logOut: () -> Unit
    ) -> Unit
}