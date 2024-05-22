package com.atech.student.ui.research.detail.compose

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.MarkDown
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResearchDetailScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    onEvent: (ResearchScreenEvents) -> Unit = {},
    isExistInWishList: Boolean = false,
    isFromArgs: Boolean = false,
    model: ResearchModel
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
            IconButton(
                onClick = {
                    onEvent.invoke(ResearchScreenEvents.OnAddToWishList(model, !isExistInWishList))
                    if (isFromArgs) {
                        onEvent.invoke(ResearchScreenEvents.ResetClickItem)
                        navController.popBackStack()
                    }
                }
            ) {
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
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
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
            Button(
                onClick = {
                    navController.navigate(
                        ResumeScreenArgs(
                            key = model.key!!,
                            question = model.questions!!
                        )
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(MaterialTheme.spacing.medium)
            ) {
                Text(
                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                    text = stringResource(id = R.string.apply)
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraLarge))
        }
    }
}

val des = """
    Lorem chudam dolor sit amet, consectetur adipiscing elit. Fusce finibus pellentesque enim varius eleifend. Cras quis justo quam. Interdum et malesuada fames ac ante ipsum primis in faucibus. Phasellus nisi diam, vestibulum dictum rutrum a, eleifend nec augue. Sed interdum egestas sapien dictum condimentum. Sed non consequat nibh. Mauris quis auctor leo. Nulla consectetur dolor leo, in sagittis diam aliquet id. Curabitur facilisis turpis egestas porta bibendum. Praesent suscipit ligula sed erat scelerisque, ut tincidunt sem congue. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Curabitur tincidunt diam eget eleifend commodo.

    Interdum et malesuada fames ac ante ipsum primis in faucibus. Donec libero risus, laoreet vel ante eget, lacinia consequat magna. Nam eget justo at metus accumsan consequat eu id nulla. Nulla egestas, enim eget blandit pretium, purus purus placerat elit, ut consequat odio neque in leo. Sed purus ante, consequat tempus risus sed, pharetra rhoncus ex. Sed sodales elit eu ultrices luctus. Sed posuere, metus quis volutpat fermentum, nisi arcu egestas orci, in euismod metus risus a sem. Phasellus sed nunc pharetra nisi interdum vulputate quis et nulla. Quisque et mattis est, volutpat porttitor sapien.

    Sed lacinia luctus urna. Nulla facilisi. Phasellus venenatis tempus pharetra. Praesent maximus iaculis mi ut maximus. Nulla elementum sem sem, et euismod turpis laoreet in. Vestibulum augue risus, rhoncus sit amet felis quis, dapibus tempor velit. Nunc metus metus, molestie a sodales et, molestie sit amet velit. Suspendisse tempor, justo et ultrices ultricies, dolor turpis consectetur nunc, sit amet volutpat neque mi in odio. Pellentesque eget tempor dui, a aliquam metus. Maecenas ipsum ligula, tempor vel nibh id, luctus faucibus tellus. Praesent iaculis blandit nibh, et pharetra justo condimentum vitae. Interdum et malesuada fames ac ante ipsum primis in faucibus. Vivamus justo libero, feugiat ac enim vitae, sollicitudin tristique mi. Fusce nec ullamcorper purus.
    
   
""".trimIndent()

@Preview(showBackground = true)
@Composable
fun DetailScreenPreview() {
    ResearchHubTheme {
        ResearchDetailScreen(
            model = ResearchModel(
                title = "This is only for preview", description = "des", tags = toJSON(
                    listOf(
                        TagModel(createdBy = "", name = "Tag 1"),
                        TagModel(createdBy = "", name = "Tag 1"),
                        TagModel(createdBy = "", name = "Tag 1"),
                        TagModel(createdBy = "", name = "Tag 1"),
                        TagModel(createdBy = "", name = "Tag 1"),
                        TagModel(createdBy = "", name = "Tag 1"),
                        TagModel(createdBy = "", name = "Tag 1"),
                        TagModel(createdBy = "", name = "Tag 1"),
                        TagModel(createdBy = "", name = "Tag 1"),
                        TagModel(createdBy = "", name = "Tag 1"),
                    )
                ), created = System.currentTimeMillis(), createdBy = "Dr. Aparna Sukla"
            ),
        )
    }
}