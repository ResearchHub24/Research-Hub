package com.atech.research.ui.screens.verify.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.utils.isValidPassword
import com.atech.research.R
import com.atech.research.navigation.ResearchHubNavigation
import com.atech.research.ui.screens.verify.VerifyScreenEvents
import com.atech.research.ui.screens.verify.VerifyScreenState
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.DisplayCard
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.PasswordEditTextCompose
import com.atech.ui_common.common.toast
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    state: VerifyScreenState = VerifyScreenState(),
    onEvent: (VerifyScreenEvents) -> Unit = {}
) {
    var hasError by rememberSaveable { mutableStateOf(Pair(false, "")) }
    val context = LocalContext.current
    var title by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(key1 = state.errorMessage) {
        if (state.errorMessage != null) {
            toast(context, state.errorMessage)
        }
    }
    MainContainer(
        modifier = modifier,
        title = title,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(MaterialTheme.spacing.medium)
                .padding(contentPadding), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (state.userData == null) {
                ProgressBar()
                return@Column
            }
            title = "Password"
            if (state.userData.password == null) {
                SetPasswordScreen(modifier = Modifier.fillMaxSize(),
                    password = state.passwordPair.first,
                    confirmPassword = state.passwordPair.second,
                    onPasswordChange = {
                        onEvent.invoke(
                            VerifyScreenEvents.OnPassWordChange(
                                state.passwordPair.copy(first = it)
                            )
                        )
                    },
                    onConfirmPasswordChange = {
                        onEvent.invoke(
                            VerifyScreenEvents.OnPassWordChange(
                                state.passwordPair.copy(second = it)
                            )
                        )
                    },
                    hasError = hasError,
                    onApplyButtonClick = {
                        if (state.passwordPair.first.length < 8) {
                            hasError = true to "Password must be at least 8 characters"
                            return@SetPasswordScreen
                        }
                        if (isValidPassword(state.passwordPair.first).not()) {
                            hasError =
                                true to "Password must contain upper and lower case character, one special character, and one number"
                            return@SetPasswordScreen
                        }
                        if (state.passwordPair.first != state.passwordPair.second) {
                            hasError = true to "Passwords do not match"
                            return@SetPasswordScreen
                        }
                        hasError = false to ""
                        onEvent.invoke(VerifyScreenEvents.SetPassword)
                    })
                return@Column
            }
            title = "Verification"
            if (state.userData.verified.not()) {
                VerifyAccount()
                return@Column
            }
            navController.navigate(
                ResearchHubNavigation.MainScreen.route
            ) {
                popUpTo(0)
            }
        }
    }
}

@Composable
private fun ProgressBar() {
    Box(
        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            trackColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(200.dp),
            strokeCap = StrokeCap.Round,
            strokeWidth = 10.dp,
        )
    }
}

@Composable
private fun SetPasswordScreen(
    modifier: Modifier = Modifier,
    password: String = "",
    confirmPassword: String = "",
    hasError: Pair<Boolean, String> = false to "",
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onApplyButtonClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.img_add_password),
            contentDescription = "Add Password",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        PasswordEditTextCompose(
            value = password,
            placeholder = "Password",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onPasswordChange
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        PasswordEditTextCompose(
            value = confirmPassword,
            placeholder = "Confirm Password",
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onConfirmPasswordChange,
            imeAction = ImeAction.Done
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        AnimatedVisibility(hasError.first) {
            DisplayCard(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    width = CardDefaults.outlinedCardBorder().width,
                    color = MaterialTheme.colorScheme.error
                ),
            ) {
                Row(
                    modifier = Modifier,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Outlined.ErrorOutline,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                    Text(
                        text = hasError.second,
                        modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        ApplyButton(
            text = "Set Password",
            horizontalPadding = MaterialTheme.spacing.default,
            action = onApplyButtonClick
        )
    }
}

@Composable
private fun VerifyAccount() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(R.drawable.img_verify),
            contentDescription = "Add Password",
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.3f)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = "To maintain the integrity of the platform, we need to verify your account.This will help us to ensure that only teachers are using the platform.",
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        Text(
            text = "This process will take a few hours. We will notify you once your account is verified.",
            textAlign = TextAlign.Justify,
            style = MaterialTheme.typography.bodySmall
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun VerifyScreenPreview() {
    ResearchHubTheme {
        VerifyAccount()
    }
}