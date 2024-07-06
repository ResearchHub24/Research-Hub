package com.atech.research.ui.activities

import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.atech.core.use_cases.IsUserLoggedInUseCase
import com.atech.core.use_cases.SignOut
import com.atech.core.utils.PrefKeys
import com.atech.core.utils.restartApplication
import com.atech.research.navigation.ResearchHubNavigation
import com.atech.research.ui.screens.login.utils.GoogleAuthUiClient
import com.atech.research.workmanager.UpdateTokenWorker
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.utils.NavigationProvider
import com.google.android.gms.auth.api.identity.Identity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.time.Duration
import java.util.concurrent.TimeUnit
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

    private val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            oneTapClient = Identity.getSignInClient(this)
        )
    }
    private val saveRequest =
        PeriodicWorkRequestBuilder<UpdateTokenWorker>(730, TimeUnit.HOURS).apply {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                setBackoffCriteria(
                    backoffPolicy = BackoffPolicy.EXPONENTIAL, duration = Duration.ofSeconds(10)
                )
            }
        }.setConstraints(
            constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        ).build()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "saveRequest", ExistingPeriodicWorkPolicy.UPDATE, saveRequest
            )
    }

    @Composable
    private fun MainScreen() {
        ResearchHubTheme {
            val navController = rememberNavController()
            val isLogInSkipp = pref.getBoolean(PrefKeys.IS_LOGIN_SKIP.value, false)
            val scope = rememberCoroutineScope()
            Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                ResearchHubNavigation(
                    navController = navController,
                    modifier = Modifier.windowInsetsPadding(
                        WindowInsets(
                            bottom = innerPadding.calculateBottomPadding(),
                        )
                    ),
                    startDestination = if (isUserLogIn.invoke() == null && !isLogInSkipp) ResearchHubNavigation.LogInScreen
                    else ResearchHubNavigation.MainScreen,
                    mainScreen = navigationProvider.provideMainScreen(),
                    navigationItem = navigationProvider.getNavigationItems(),
                    visibleScreens = navigationProvider.getVisibleScreens()
                ) {
                    scope.launch {
                        googleAuthUiClient.signOut {
                            signOut.invoke {
                                restartApplication()
                            }
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
    ResearchHubTheme {}
}