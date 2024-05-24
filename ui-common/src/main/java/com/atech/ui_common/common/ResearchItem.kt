package com.atech.ui_common.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PublicOff
import androidx.compose.material.icons.rounded.PublishedWithChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atech.core.model.ResearchModel
import com.atech.core.model.TagModel
import com.atech.core.utils.fromJsonList
import com.atech.ui_common.R
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.SelectedChipContainColor
import com.atech.ui_common.theme.SelectedChipContainerColor
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResearchItem(
    modifier: Modifier = Modifier,
    model: ResearchModel,
    isSelected: Boolean = false,
    canShowViewDetailButton: Boolean = true,
    onClick: () -> Unit = {},
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        border = BorderStroke(.5.dp, MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f))
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    horizontal = MaterialTheme.spacing.medium,
                ),
            shape = RoundedCornerShape(0.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        horizontal = MaterialTheme.spacing.medium,
                        vertical = MaterialTheme.spacing.small
                    ),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier,
                    text = model.title ?: "No Title",
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                Row(
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Person,
                        contentDescription = null,
                        modifier = Modifier.size(
                            MaterialTheme.typography.bodyMedium.fontSize.value.dp
                        )
                    )
                    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                    Text(
                        text = model.createdBy ?: "No Author",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.FilterList,
                        contentDescription = null,
                        modifier = Modifier.size(
                            MaterialTheme.typography.bodyLarge.fontSize.value.dp
                        )
                    )
                    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                    model.tags?.let {
                        val tags = fromJsonList<TagModel>(it)
                        Text(
                            modifier = Modifier.basicMarquee(),
                            text = tags.take(5).joinToString(", ") { tagModel -> tagModel.name },
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    } ?: Text(
                        text = "No Tags",
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PublishedWithChanges,
                            contentDescription = null,
                            modifier = Modifier.size(
                                MaterialTheme.typography.bodyLarge.fontSize.value.dp
                            )
                        )
                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                        Text(
                            text = model.formattedTime,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.extraSmall))
                    Row(
                        modifier = Modifier,
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.PublicOff,
                            contentDescription = null,
                            modifier = Modifier.size(
                                MaterialTheme.typography.bodyLarge.fontSize.value.dp
                            )
                        )
                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                        Text(
                            text = model.formattedDeadline,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (isSelected)
                        FilterChip(
                            modifier = Modifier
                                .align(Alignment.BottomStart),
                            label = {
                                Text(stringResource(R.string.selected))
                            },
                            onClick = {

                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Celebration,
                                    contentDescription = null
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedLabelColor = SelectedChipContainColor,
                                selectedLeadingIconColor = SelectedChipContainColor,
                                selectedContainerColor = SelectedChipContainerColor
                            ),
                            selected = true
                        )
                    if (canShowViewDetailButton)
                        TextButton(
                            onClick = onClick,
                            modifier = Modifier.align(Alignment.BottomEnd)
                        ) {
                            Text(text = stringResource(id = R.string.view_details))
                        }
                }
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun ResearchItemPreview() {
    ResearchHubTheme {
        ResearchItem(
            model = ResearchModel(
                title = "Finding the best way to learn",
                createdBy = "John Doe",
                created = System.currentTimeMillis(),
            ),
            isSelected = true
        )
    }
}