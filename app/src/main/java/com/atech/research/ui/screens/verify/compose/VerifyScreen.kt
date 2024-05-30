package com.atech.research.ui.screens.verify.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VerifyScreen(
    modifier: Modifier = Modifier
) {
    MainContainer(
        modifier = modifier,
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .padding(contentPadding)
        ) {
            Text("Verify Screen")
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