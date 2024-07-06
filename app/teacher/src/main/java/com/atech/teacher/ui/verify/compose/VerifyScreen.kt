package com.atech.teacher.ui.verify.compose

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.core.utils.isValidPassword
import com.atech.teacher.navigation.MainScreenRoutes
import com.atech.teacher.navigation.ResearchRoutes
import com.atech.teacher.ui.verify.VerifyScreenEvents
import com.atech.teacher.ui.verify.VerifyScreenState
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ProgressBar
import com.atech.ui_common.common.toast
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onVerifyScreenEvent: (VerifyScreenEvents) -> Unit = {},
    verifyScreen: VerifyScreenState = VerifyScreenState(),
) {
    val context = LocalContext.current
    var title by rememberSaveable { mutableStateOf("") }
    var hasError by rememberSaveable { mutableStateOf(Pair(false, "")) }
    LaunchedEffect(key1 = verifyScreen.errorMessage) {
        if (verifyScreen.errorMessage != null) {
            toast(context, verifyScreen.errorMessage)
        }
    }
    MainContainer(
        title = title
    ) { paddingValues ->
        if (verifyScreen.userData == null) {
            ProgressBar(paddingValues)
            return@MainContainer
        }
        if (verifyScreen.userData.password == null) {
            title = "Set Password"
            SetPasswordScreen(modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
                .padding(paddingValues),
                password = verifyScreen.passwordPair.first,
                confirmPassword = verifyScreen.passwordPair.second,
                onPasswordChange = {
                    onVerifyScreenEvent.invoke(
                        VerifyScreenEvents.OnPassWordChange(
                            verifyScreen.passwordPair.copy(first = it)
                        )
                    )
                },
                onConfirmPasswordChange = {
                    onVerifyScreenEvent.invoke(
                        VerifyScreenEvents.OnPassWordChange(
                            verifyScreen.passwordPair.copy(second = it)
                        )
                    )
                },
                hasError = hasError,
                onApplyButtonClick = {
                    if (verifyScreen.passwordPair.first.length < 8) {
                        hasError = true to "Password must be at least 8 characters"
                        return@SetPasswordScreen
                    }
                    if (isValidPassword(verifyScreen.passwordPair.first).not()) {
                        hasError =
                            true to "Password must contain upper and lower case character, one special character, and one number"
                        return@SetPasswordScreen
                    }
                    if (verifyScreen.passwordPair.first != verifyScreen.passwordPair.second) {
                        hasError = true to "Passwords do not match"
                        return@SetPasswordScreen
                    }
                    hasError = false to ""
                    onVerifyScreenEvent.invoke(VerifyScreenEvents.SetPassword)
                })
            return@MainContainer
        }
        if (verifyScreen.userData.verified.not()) {
            title = "Verify Account"
            VerifyAccount(
                modifier
                    .padding(paddingValues)
                    .padding(MaterialTheme.spacing.medium),
            )
            return@MainContainer
        }
        if (verifyScreen.userData.verified && verifyScreen.userData.password.isNullOrEmpty()
                .not()
        ) {
            navController.navigate(
                ResearchRoutes.ResearchScreen.route
            ) {
                popUpTo(MainScreenRoutes.VerifyScreen.route) {
                    inclusive = true
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun VerifyScreenPreview() {
    ResearchHubTheme {
        VerifyScreen()
    }
}