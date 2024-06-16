package com.atech.student.ui.research.detail.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bookmark
import androidx.compose.material.icons.rounded.BookmarkBorder
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.ResearchModel
import com.atech.core.model.TagModel
import com.atech.core.utils.fromJsonList
import com.atech.core.utils.toJSON
import com.atech.student.navigation.ResumeScreenArgs
import com.atech.student.ui.research.main.ResearchScreenEvents
import com.atech.ui_common.R
import com.atech.ui_common.common.BottomPadding
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.MarkDown
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ResearchDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onEvent: (ResearchScreenEvents) -> Unit = {},
    isExistInWishList: Boolean = false,
    isFromArgs: Boolean = false,
    model: ResearchModel,
    filledForm: String = "",
    isUserLogIn: Boolean = true,
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    MainContainer(title = stringResource(id = R.string.blank),
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        onNavigationClick = {
            onEvent.invoke(ResearchScreenEvents.ResetClickItem)
            navController.popBackStack()
        },
        scrollBehavior = scrollBehavior,
        actions = {
            IconButton(onClick = {
                onEvent.invoke(ResearchScreenEvents.OnAddToWishList(model, !isExistInWishList))
                if (isFromArgs) {
                    onEvent.invoke(ResearchScreenEvents.ResetClickItem)
                    navController.popBackStack()
                }
            }) {
                Icon(
                    imageVector = if (isExistInWishList) Icons.Rounded.Bookmark else Icons.Rounded.BookmarkBorder,
                    contentDescription = stringResource(id = R.string.wishlist)
                )
            }
        }) { paddingValue ->
        Column(
            modifier = Modifier
                .verticalScroll(state = rememberScrollState())
                .padding(paddingValue)
                .padding(horizontal = MaterialTheme.spacing.medium),
        ) {
            Text(
                text = model.title ?: "No Title", style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = model.createdBy ?: "No Author", style = MaterialTheme.typography.bodyMedium
            )
            model.tags?.let { tagsString ->
                Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                val tags = fromJsonList<TagModel>(tagsString)
                Text(
                    text = "Tags", style = MaterialTheme.typography.labelSmall
                )
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                ) {
                    tags.forEach { item ->
                        FilterChip(selected = true,
                            onClick = {},
                            label = { Text(text = item.name) })
                    }
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            MarkDown(markDown = model.description ?: "")
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            val isFilled = filledForm.contains(model.key ?: "")
            Button(
                onClick = {
                    navController.navigate(
                        ResumeScreenArgs(
                            key = model.key ?: "", question = model.questions ?: ""
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(MaterialTheme.spacing.medium),
                enabled = isUserLogIn && isFilled.not()
            ) {
                Text(
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                    text = stringResource(
                        if (isUserLogIn.not()) R.string.login_to_apply else
                            if (isFilled) R.string.already_apply else R.string.apply
                    )
                )
            }
            BottomPadding()
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    ResearchHubTheme {
        ResearchDetailScreen(
            model = ResearchModel(
                title = "This is only for preview", description = "des", tags = toJSON(
                    listOf(
                        TagModel(name = "Tag 1"),
                        TagModel(name = "Tag 1"),
                        TagModel(name = "Tag 1"),
                        TagModel(name = "Tag 1"),
                        TagModel(name = "Tag 1"),
                        TagModel(name = "Tag 1"),
                        TagModel(name = "Tag 1"),
                        TagModel(name = "Tag 1"),
                        TagModel(name = "Tag 1"),
                        TagModel(name = "Tag 1"),
                    )
                ), created = System.currentTimeMillis(), createdBy = "Dr. Aparna Sukla"
            ),
        )
    }
}