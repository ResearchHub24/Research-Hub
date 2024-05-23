package com.atech.teacher.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.atech.ui_common.utils.NavBarModel
import com.atech.ui_common.utils.NavigationProvider

class TeacherNavigationProvider : NavigationProvider {
    override fun getVisibleScreens(): List<String> {
        return listOf()
    }

    override fun getNavigationItems(): List<NavBarModel> {
        return listOf()
    }

    @Composable
    override fun provideMainScreen(): @Composable (navController: NavHostController, modifier: Modifier, navigateToLogIn: () -> Unit) -> Unit =
        { _, modifier, _ ->
            Column(
                modifier = modifier
            ) {
                Text("Teacher")
            }
        }
}