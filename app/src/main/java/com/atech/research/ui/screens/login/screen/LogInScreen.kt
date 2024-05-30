package com.atech.research.ui.screens.login.screen

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.utils.restartApplication
import com.atech.research.R
import com.atech.research.navigation.ResearchHubNavigation
import com.atech.research.ui.common.GoogleButton
import com.atech.research.ui.screens.login.LogInScreenEvents
import com.atech.research.ui.screens.login.LogInState
import com.atech.research.ui.screens.login.utils.GoogleAuthUiClient
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing
import com.atech.ui_common.utils.IsStudent
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    logInState: LogInState,
    onEvent: (LogInScreenEvents) -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var logInMessage by rememberSaveable { mutableStateOf("Creating Account...") }
    var hasClick by rememberSaveable { mutableStateOf(false) }
    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            oneTapClient = Identity.getSignInClient(context)
        )
    }
    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = { result ->
                if (result.resultCode == RESULT_OK) {
                    coroutineScope.launch {
                        val signInResult = googleAuthUiClient.signInWithIntent(
                            data = result.data ?: return@launch
                        )
                        onEvent(
                            LogInScreenEvents.OnSignInResult(
                                LogInState(
                                    token = signInResult.first,
                                    errorMessage = signInResult.second?.message
                                )
                            )
                        )
                    }
                }
            })
    LaunchedEffect(key1 = logInState.errorMessage) {
        logInState.errorMessage?.let { error ->
            hasClick = false
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
            googleAuthUiClient.signOut {
                onEvent.invoke(LogInScreenEvents.PreformLogOutOnError)
            }
        }
    }
    LaunchedEffect(key1 = logInState) {
        logInState.token?.let { token ->
            logInMessage = "Signing In... 🔃"
            onEvent(LogInScreenEvents.TriggerAuth(token))
        }
        logInState.uId?.let {
            logInMessage = "Sign Done"
            context.restartApplication()
        }
    }
    MainContainer(
        modifier = modifier, appBarColor = MaterialTheme.colorScheme.primary
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Box(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .fillMaxWidth()
                    .fillMaxHeight(.5f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Logo",
                    modifier = modifier
                        .fillMaxWidth()
                        .fillMaxHeight()
                )
            }
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.spacing.medium)
                    .fillMaxWidth()
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = stringResource(id = R.string.app_welcome))
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                GoogleButton(
                    loadingText = logInMessage,
                    hasClick = hasClick,
                    hasClickChange = { value ->
                        hasClick = value
                    }
                ) {
                    hasClick = true
                    coroutineScope.launch {
                        val sigInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                sigInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                }
                IsStudent {
                    TextButton(
                        modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        onClick = {
                            navigateToHome(navHostController)
                            onEvent(LogInScreenEvents.OnSkipClick)
                        }
                    ) {
                        Text(text = stringResource(id = R.string.skip))
                    }
                }
            }
        }
    }
}


private fun navigateToHome(navHostController: NavHostController) {
    navHostController.navigate(ResearchHubNavigation.MainScreen.route) {
        popUpTo(ResearchHubNavigation.LogInScreen.route) {
            inclusive = true
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LogInScreenPreview() {
    ResearchHubTheme {
        LogInScreen(logInState = LogInState(), onEvent = {})
    }
}