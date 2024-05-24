package com.atech.research.ui.activities

import android.content.SharedPreferences
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
import com.atech.core.use_cases.SignOut
import com.atech.core.utils.PrefKeys
import com.atech.core.utils.restartApplication
import com.atech.research.navigation.ResearchHubNavigation
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.utils.NavigationProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var isUserLogIn: IsUserLoggedInUseCase

    @Inject
    lateinit var pref: SharedPreferences

    @Inject
    lateinit var navigationProvider: NavigationProvider

    @Inject
    lateinit var signOut: SignOut

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ResearchHubTheme {
                val navController = rememberNavController()
                val isLogInSkipp = pref.getBoolean(PrefKeys.IS_LOGIN_SKIP.value, false)
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ResearchHubNavigation(
                        navController = navController,
                        modifier = Modifier.windowInsetsPadding(
                            WindowInsets(
                                bottom = innerPadding.calculateBottomPadding(),
                            )
                        ),
                        startDestination = if (isUserLogIn.invoke() == null && !isLogInSkipp)
                            ResearchHubNavigation.LogInScreen
                        else ResearchHubNavigation.MainScreen,
                        mainScreen = navigationProvider.provideMainScreen(),
                        navigationItem = navigationProvider.getNavigationItems(),
                        visibleScreens = navigationProvider.getVisibleScreens()
                    ) {
                        signOut.invoke {
                            this.restartApplication()
                        }
                    }
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