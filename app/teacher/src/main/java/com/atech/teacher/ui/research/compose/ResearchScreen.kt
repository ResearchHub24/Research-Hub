package com.atech.teacher.ui.research.compose

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.ResearchModel
import com.atech.core.utils.isValidPassword
import com.atech.teacher.navigation.AddEditScreenArgs
import com.atech.teacher.ui.research.VerifyScreenEvents
import com.atech.teacher.ui.research.VerifyScreenState
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ResearchTeacherItem
import com.atech.ui_common.common.toast
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResearchScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    verifyScreen: VerifyScreenState = VerifyScreenState(),
    state: List<ResearchModel> = emptyList(),
    onVerifyScreenEvent: (VerifyScreenEvents) -> Unit = {}
) {
    val context = LocalContext.current
    var hasError by rememberSaveable { mutableStateOf(Pair(false, "")) }
    var title by rememberSaveable { mutableStateOf("") }
    LaunchedEffect(key1 = verifyScreen.errorMessage) {
        if (verifyScreen.errorMessage != null) {
            toast(context, verifyScreen.errorMessage)
        }
    }
    val floatingActionButton: @Composable () -> Unit =
        if (verifyScreen.userData?.verified == true) {
            {
                FloatingActionButton(onClick = {
                    navHostController.navigate(AddEditScreenArgs(key = null))
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Add, contentDescription = null
                    )
                }
            }
        } else {
            {}
        }
    MainContainer(
        modifier = modifier,
        title = title,
        floatingActionButton = floatingActionButton
    ) { paddingValues ->
        if (verifyScreen.userData == null) {
            ProgressBar(paddingValues)
            return@MainContainer
        }
        if (state.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier.padding(paddingValues)
            )
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
        title = "Your Posted Research"
        LazyColumn(
            modifier = Modifier, contentPadding = paddingValues
        ) {
            items(state) {
                ResearchTeacherItem(
                    model = it
                )
            }
        }
    }
}

@Composable
private fun ProgressBar(
    paddingValues: PaddingValues
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            trackColor = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.size(200.dp),
            strokeCap = StrokeCap.Round,
            strokeWidth = 10.dp,
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ResearchScreenPreview() {
    ResearchHubTheme {
        ResearchScreen()
    }
}