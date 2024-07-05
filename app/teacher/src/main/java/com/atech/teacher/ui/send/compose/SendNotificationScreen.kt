package com.atech.teacher.ui.send.compose

import android.os.Handler
import android.os.Looper
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.teacher.R
import com.atech.teacher.navigation.SendNotificationScreenArgs
import com.atech.teacher.ui.send.SendNotificationEvents
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.EditTextEnhance
import com.atech.ui_common.common.LottieAnim
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendNotificationScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    args: SendNotificationScreenArgs,
    title: String,
    onEvent: (SendNotificationEvents) -> Unit = {}
) {
    var canGoBack by rememberSaveable { mutableStateOf(true) }

    BackHandler(enabled = canGoBack) {
        navController.navigateUp()
    }

    LifecycleEventEffect(event = Lifecycle.Event.ON_START) {
        onEvent.invoke(
            SendNotificationEvents.GetDataFromArgs(
                args = args
            )
        )
    }
    MainContainer(modifier = modifier, title = "Send Notification", onNavigationClick = {
        if (canGoBack) navController.navigateUp()
    }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
        ) {
            AnimatedVisibility(!canGoBack) {
                ShowNotificationDialog(onDismissRequest = {
                    canGoBack = true
                })
            }
            EditTextEnhance(
                modifier = Modifier.fillMaxWidth(),
                value = title,
                isError = title.isEmpty() || title.length > 50,
                onValueChange = {
                    onEvent.invoke(SendNotificationEvents.OnTitleChange(it))
                },
                clearIconClick = {
                    onEvent.invoke(SendNotificationEvents.OnTitleChange(""))
                },
                supportingText = {
                    Text(
                        if (title.isEmpty()) "Body can't be empty"
                        else if (title.length > 50) "Body must be less than 50 characters"
                        else "${title.length}/50"
                    )
                },
                placeholder = "Title"
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            ApplyButton(
                enable = title.isNotBlank() && title.length <= 50,
                text = "Send",
                horizontalPadding = MaterialTheme.spacing.default
            ) {
                canGoBack = false
                onEvent.invoke(SendNotificationEvents.SendNotification(onSuccess = {
                    Handler(Looper.getMainLooper()).post {
                        navController.navigateUp()
                        canGoBack = true
                    }
                }))
            }
        }
    }
}

@Composable
private fun ShowNotificationDialog(
    onDismissRequest: () -> Unit = {}
) {
    Dialog(
        onDismissRequest = onDismissRequest, properties = DialogProperties(
            dismissOnBackPress = false, dismissOnClickOutside = false
        )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.large),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                    text = "Research Publish Notification"
                )
                LottieAnim(
                    res = R.raw.sending
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SendNotificationScreenPreview() {
    ResearchHubTheme {
        SendNotificationScreen(
            args = SendNotificationScreenArgs("", "", 1L),
            title = "Title",
        )
//        ShowNotificationDialog()
    }
}