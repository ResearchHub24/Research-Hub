package com.atech.ui_common.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import com.atech.ui_common.theme.spacing

@Composable
fun ApplyButton(
    text: String,
    modifier: Modifier = Modifier,
    enable: Boolean = true,
    horizontalPadding: Dp = MaterialTheme.spacing.medium,
    action: () -> Unit
) {
    TextButton(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding),
        onClick = action,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = RoundedCornerShape(MaterialTheme.spacing.medium),
        enabled = enable
    ) {
        Text(
            modifier = Modifier.padding(MaterialTheme.spacing.medium), text = text
        )
    }
}