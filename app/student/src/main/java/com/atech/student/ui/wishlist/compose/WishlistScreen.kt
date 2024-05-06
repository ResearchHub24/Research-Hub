package com.atech.student.ui.wishlist.compose

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.atech.ui_common.R
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    modifier: Modifier = Modifier
) {
    MainContainer(
        title = stringResource(id = R.string.wishlist),
        modifier = modifier
    ) { paddingValues ->

    }
}

@Preview(showBackground = true)
@Composable
fun WishlistScreenPreview() {
    ResearchHubTheme {
        WishlistScreen()
    }
}