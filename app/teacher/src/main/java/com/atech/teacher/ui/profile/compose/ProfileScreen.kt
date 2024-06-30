package com.atech.teacher.ui.profile.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    logOut: () -> Unit = {}
) {
    MainContainer(
        modifier = modifier,
        title = "Profile"
    ) { paddingValues ->
        GlobalEmptyScreen(
            modifier = Modifier.padding(paddingValues)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun ProfileScreenPreview() {
    ResearchHubTheme {
        ProfileScreen()
    }
}