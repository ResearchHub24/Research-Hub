package com.atech.ui_common.common

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FilterList
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.PublicOff
import androidx.compose.material.icons.rounded.PublishedWithChanges
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atech.core.model.ResearchModel
import com.atech.core.model.TagModel
import com.atech.core.utils.fromJsonList
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ResearchItem(
    modifier: Modifier = Modifier, model: ResearchModel, onClick: () -> Unit = {}
) {
    Log.d("AAA", "ResearchItem: $model")
    Surface(modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }) {
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
                    .padding(horizontal = MaterialTheme.spacing.medium),
                verticalArrangement = Arrangement.Top
            ) {
                Text(
                    modifier = Modifier,
                    text = model.title,
                    style = MaterialTheme.typography.titleLarge,
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
                        text = model.createdBy,
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
                    model.tags.let {
                        val tags = fromJsonList<TagModel>(it)
                        Text(
                            modifier = Modifier.basicMarquee(),
                            text = tags.take(5).joinToString(", ") { tagModel -> tagModel.name },
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
//                    Text(
//                        modifier = Modifier.basicMarquee(),
//                        text = model.tagsToList.joinToString(".").take(5).ifEmpty { "No Tags" },
//                        style = MaterialTheme.typography.labelSmall,
//                        maxLines = 1,
//                        overflow = TextOverflow.Ellipsis
//                    )
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
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
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

            )
        )
    }
}