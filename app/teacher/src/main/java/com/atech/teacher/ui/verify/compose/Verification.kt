package com.atech.teacher.ui.verify.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.atech.teacher.R
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.DisplayCard
import com.atech.ui_common.common.PasswordEditTextCompose
import com.atech.ui_common.theme.spacing

@Composable
fun SetPasswordScreen(
    modifier: Modifier = Modifier,
    password: String = "",
    confirmPassword: String = "",
    hasError: Pair<Boolean, String> = false to "",
    onPasswordChange: (String) -> Unit = {},
    onConfirmPasswordChange: (String) -> Unit = {},
    onApplyButtonClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
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
fun VerifyAccount(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
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