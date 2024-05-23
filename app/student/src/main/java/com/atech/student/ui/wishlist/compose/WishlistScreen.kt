package com.atech.student.ui.wishlist.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.ResearchModel
import com.atech.student.navigation.ResearchScreenRoutes
import com.atech.ui_common.R
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.ResearchItem
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WishlistScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    items: List<ResearchModel> = emptyList(),
) {
    MainContainer(
        title = stringResource(id = R.string.wishlist),
        modifier = modifier,
        onNavigationClick = {
            navController.popBackStack()
        }
    ) { paddingValues ->
        if (items.isEmpty()) {
            GlobalEmptyScreen(
                modifier = Modifier
                    .padding(paddingValues),
                title = stringResource(id = R.string.no_wishlist_found),
            )
            return@MainContainer
        }
        LazyColumn(
            contentPadding = paddingValues,
            modifier = Modifier,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(items.size) { index ->
                ResearchItem(model = items[index]) {
                    navController.navigate(ResearchScreenRoutes.DetailScreen.route + "?key=${items[index].key}")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WishlistScreenPreview() {
    ResearchHubTheme {
        WishlistScreen()
    }
}