package com.atech.research.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.atech.core.use_cases.IsUserLoggedInUseCase
import com.atech.research.navigation.ResearchHubNavigation
import com.atech.ui_common.theme.ResearchHubTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var isUserLogIn: IsUserLoggedInUseCase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResearchHubTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ResearchHubNavigation(
                        navController = navController,
                        modifier = Modifier.windowInsetsPadding(
                            WindowInsets(
                                bottom = innerPadding.calculateBottomPadding(),
                            )
                        ),
                        startDestination = if (isUserLogIn.invoke() == null)
                            ResearchHubNavigation.LogInScreen
                        else
                            ResearchHubNavigation.MainScreen
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ResearchHubTheme {
    }
}