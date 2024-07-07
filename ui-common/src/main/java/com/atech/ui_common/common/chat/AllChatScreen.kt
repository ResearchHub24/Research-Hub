package com.atech.ui_common.common.chat

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.AllChatModel
import com.atech.core.utils.getDate
import com.atech.ui_common.R
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.ImageLoaderRounderCorner
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.captionColor
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllChatScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onClick: (AllChatModel) -> Unit = {},
    forAdmin: Boolean = true,
    state: List<AllChatModel> = emptyList()
) {
    val scrollBarBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(
        scrollBehavior = scrollBarBehavior,
        modifier = modifier,
        title = stringResource(R.string.all_chats),
        onNavigationClick = {
            navController.navigateUp()
        }
    ) { paddingValues ->
        if (state.isEmpty()) {
            GlobalEmptyScreen(
                Modifier.padding(paddingValues),
                title = stringResource(R.string.no_chats_yet)
            )
            return@MainContainer
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .nestedScroll(connection = scrollBarBehavior.nestedScrollConnection),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            items(state) {
                AllChatItems(
                    model = it, onClick = { onClick.invoke(it) },
                    forAdmin = forAdmin
                )
            }
        }
    }
}

@Composable
fun AllChatItems(
    modifier: Modifier = Modifier,
    model: AllChatModel,
    forAdmin: Boolean = true,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick.invoke() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            colors = CardDefaults.cardColors()
                .copy(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                )
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ImageLoaderRounderCorner(
                    modifier = Modifier.size(70.dp),
                    imageUrl = model.receiverProfileUrl,
                    isRounderCorner = 40.dp
                )
                Spacer(Modifier.width(MaterialTheme.spacing.medium))
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.basicMarquee(),
                        text = if (forAdmin) model.receiverName else model.senderName,
                        style = MaterialTheme.typography.titleMedium,
                        maxLines = 1
                    )
                    Text(
                        text = "Chat created : ${model.createdAt.getDate().trim()}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.captionColor
                    )
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun AllChatScreenPreview() {
    ResearchHubTheme {
        AllChatScreen()
//        AllChatItems(
//            model = AllChatModel(
//                receiverName = "To no one",
//            )
//        )
    }
}