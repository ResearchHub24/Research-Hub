package com.atech.ui_common.common

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.OpenInNew
import androidx.compose.material.icons.rounded.AlternateEmail
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material.icons.rounded.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atech.core.model.TeacherUserModel
import com.atech.core.retrofit.FacultyModel
import com.atech.core.utils.fromJsonList
import com.atech.ui_common.R
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing
import com.atech.ui_common.utils.openLink


@Composable
fun FacultyItem(
    modifier: Modifier = Modifier, model: FacultyModel
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(value = isExpanded, onValueChange = { isExpanded = it }),
        border = BorderStroke(
            .5.dp,
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
        )
    ) {
        Card(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ), shape = RoundedCornerShape(0.dp), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(0.7f)
                    ) {
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
                                text = model.name,
                                style = if (isExpanded) MaterialTheme.typography.titleMedium
                                else MaterialTheme.typography.titleSmall,
                                modifier = Modifier
//                                    .animateContentSize()
                            )
                        }

                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Work,
                                contentDescription = null,
                                modifier = Modifier.size(
                                    MaterialTheme.typography.bodyMedium.fontSize.value.dp
                                )
                            )
                            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                            Text(
                                text = model.profileData,
                                style = if (isExpanded) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                                modifier = Modifier
//                                    .animateContentSize()
                            )
                        }
                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AlternateEmail,
                                contentDescription = null,
                                modifier = Modifier.size(
                                    MaterialTheme.typography.bodyMedium.fontSize.value.dp
                                )
                            )
                            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                            Text(
                                text = model.email,
                                style = if (isExpanded) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                                modifier = Modifier
//                                    .animateContentSize()
                            )
                        }
                    }
                    ImageLoaderRounderCorner(
                        modifier = Modifier
                            .animateContentSize(
                                animationSpec = spring()
                            )
                            .height(if (isExpanded) 80.dp else 60.dp),

                        imageUrl = model.imageUrl, isRounderCorner = 100.dp
                    )
                }
                AnimatedVisibility(
                    isExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = MaterialTheme.spacing.small),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = model.areaOfInterest,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.weight(.9f)
                        )
                        if (model.profileUrl.isNotBlank()) IconButton(
                            onClick = {
                                context.openLink(model.profileUrl)
                            },
                            modifier = Modifier.padding(MaterialTheme.spacing.small),

                            ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.OpenInNew,
                                contentDescription = null,
                            )
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun RegisterFacultyItem(
    modifier: Modifier = Modifier, model: TeacherUserModel
) {
    val context = LocalContext.current
    var isExpanded by remember { mutableStateOf(false) }
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .toggleable(value = isExpanded, onValueChange = { isExpanded = it }),
        border = BorderStroke(
            .5.dp,
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
        )
    ) {
        Card(
            modifier = Modifier.padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ), shape = RoundedCornerShape(0.dp), colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(0.7f)
                    ) {
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
                                text = model.name,
                                style = if (isExpanded) MaterialTheme.typography.titleMedium
                                else MaterialTheme.typography.titleSmall,
                                modifier = Modifier
//                                    .animateContentSize()
                            )
                        }

                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AlternateEmail,
                                contentDescription = null,
                                modifier = Modifier.size(
                                    MaterialTheme.typography.bodyMedium.fontSize.value.dp
                                )
                            )
                            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                            Text(
                                text = model.email,
                                style = if (isExpanded) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                                modifier = Modifier
//                                    .animateContentSize()
                            )
                        }
                        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                        Row(
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Update,
                                contentDescription = null,
                                modifier = Modifier.size(
                                    MaterialTheme.typography.bodyMedium.fontSize.value.dp
                                )
                            )
                            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                            Text(
                                text = "User since ${model.formatedTime}",
                                style = if (isExpanded) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall,
                                modifier = Modifier
                            )
                        }
                    }
                    ImageLoaderRounderCorner(
                        modifier = Modifier
                            .animateContentSize(
                                animationSpec = spring()
                            )
                            .height(if (isExpanded) 80.dp else 60.dp),

                        imageUrl = model.photoUrl, isRounderCorner = 100.dp
                    )
                }
                AnimatedVisibility(
                    isExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.small))
                    model.links?.let { links ->
                        fromJsonList<String>(links)
                            .let { linkList ->
                                if (linkList.isNotEmpty())
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(bottom = MaterialTheme.spacing.small),
                                    ) {
                                        TitleComposable(
                                            title = stringResource(R.string.link),
                                        )
                                        linkList.forEach {
                                            TextItem(
                                                text = it,
                                                onClick = {
                                                    context.openLink(it)
                                                }
                                            )
                                        }
                                    }
                            }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FacultyItemPreview() {
    ResearchHubTheme {

    }
}