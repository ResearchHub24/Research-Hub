package com.atech.research.ui.screens.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.atech.research.ui.comman.AppBar
import com.atech.research.ui.navigation.HomeScreenRoutes
import com.atech.research.ui.navigation.MainScreenNavigation


@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
) {
    val visibleScreens = listOf(
        HomeScreenRoutes.HomeScreen.route,
    )
    val backStackEntry by navHostController.currentBackStackEntryAsState()
    Scaffold(
        modifier = modifier,
        bottomBar = {
            val currentDestination = backStackEntry?.destination
            val isTheir = visibleScreens.any { it == currentDestination?.route }
            val density = LocalDensity.current
            AnimatedVisibility(
                visible = isTheir,
                enter = slideInVertically {
                    with(density) { -40.dp.roundToPx() }
                } + expandVertically(
                    expandFrom = Alignment.Top
                ) + fadeIn(
                    initialAlpha = 0.3f
                ),
                exit = slideOutVertically() + shrinkVertically() + fadeOut()
            ) {
                AppBar(
                    backStackEntry = navHostController.currentBackStackEntryAsState(),
                    onClick = {
                        navHostController.navigate(it)
                    }
                )
            }
        }
    ) {
        MainScreenNavigation(
            navHostController = navHostController,
            modifier = modifier.padding(
                start = it.calculateStartPadding(layoutDirection = LayoutDirection.Ltr),
                end = it.calculateStartPadding(layoutDirection = LayoutDirection.Rtl),
                bottom = it.calculateBottomPadding()
            )
        )
    }
}