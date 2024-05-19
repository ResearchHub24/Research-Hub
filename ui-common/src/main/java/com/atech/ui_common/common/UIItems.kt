package com.atech.ui_common.common

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.captionColor
import com.atech.ui_common.theme.spacing

@Composable
fun TitleComposable(
    modifier: Modifier = Modifier,
    title: String
) {
    Text(
        modifier = modifier
            .fillMaxWidth()
            .padding(MaterialTheme.spacing.medium),
        text = title,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.captionColor
    )
}

@Preview(showBackground = true)
@Composable
private fun TitleComposablePreview() {
    ResearchHubTheme {
        TitleComposable(
            title = "Demo"
        )
    }
}