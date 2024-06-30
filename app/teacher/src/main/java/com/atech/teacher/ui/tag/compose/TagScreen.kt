package com.atech.teacher.ui.tag.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.TagModel
import com.atech.teacher.ui.add.AddEditScreenEvent
import com.atech.teacher.ui.tag.TagScreenEvents
import com.atech.ui_common.R
import com.atech.ui_common.common.AppAlertDialog
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.DisplayCard
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.GlobalEmptyScreen
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    tags: List<Pair<TagModel, Boolean>> = emptyList(),
    selectedTags: List<TagModel> = emptyList(),
    errorMessage: String? = null,
    onEvent: (TagScreenEvents) -> Unit = {},
    onTagChangeEvents: (AddEditScreenEvent) -> Unit = {}

) {
    var query by remember { mutableStateOf("") }
    var isDeleteDialogVisible by remember { mutableStateOf(false) }
    var currentClickItemIndex by remember { mutableIntStateOf(-1) }
    var tagList by remember { mutableStateOf(selectedTags) }
    MainContainer(modifier = modifier,
        title = stringResource(R.string.add_or_remove_tag),
        onNavigationClick = {
            navController.popBackStack()
        }) { paddingValue ->
        AnimatedVisibility(
            isDeleteDialogVisible
        ) {
            AppAlertDialog(
                dialogTitle = stringResource(R.string.remove_tag),
                dialogText = stringResource(R.string.add_or_remove_tag_message),
                icon = Icons.Outlined.Error,
                onDismissRequest = {
                    isDeleteDialogVisible = false
                    currentClickItemIndex = -1
                },
            ) {
                if (currentClickItemIndex == -1) {
                    isDeleteDialogVisible = false
                    return@AppAlertDialog
                }
                isDeleteDialogVisible = false
                onEvent(TagScreenEvents.OnDeleteTagClick(tags[currentClickItemIndex].first))
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValue)
                .padding(
                    MaterialTheme.spacing.medium
                )
        ) {

            EditText(
                modifier = Modifier.fillMaxWidth(),
                value = query,
                placeholder = stringResource(R.string.tag),
                supportingMessage = stringResource(R.string.require),
                isError = false,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Person, contentDescription = null
                    )
                },
                onValueChange = { value ->
                    query = value

                },
                clearIconClick = {
                    query = ""

                },
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                )
            )
            AnimatedVisibility(
                visible = query.isNotEmpty()
            ) {
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                ApplyButton(
                    text = stringResource(R.string.create_new_tag),
                ) {
                    onEvent(TagScreenEvents.OnCreateNewTag(
                        TagModel(
                            name = query.trim()
                        )
                    ) {
                        query = ""
                    })
                }
            }
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            AnimatedVisibility(
                visible = errorMessage != null
            ) {
                Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                DisplayCard(
                    modifier = Modifier,
                    border = BorderStroke(
                        width = CardDefaults.outlinedCardBorder().width,
                        color = MaterialTheme.colorScheme.error
                    ),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.ErrorOutline,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = errorMessage!!,
                            modifier = Modifier.padding(MaterialTheme.spacing.medium),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            AnimatedVisibility(
                visible = tags.isEmpty()
            ) {
                GlobalEmptyScreen(
                    title = stringResource(R.string.no_tags_found),
                )
            }
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(
                    MaterialTheme.spacing.medium
                )
            ) {
                items(tags.size) { index ->
                    TagItem(
                        item = tags[index],
                        onDeleteClick = {
                            currentClickItemIndex = index
                            isDeleteDialogVisible = true
                        },
                        checked = selectedTags.contains(tags[index].first),
                        onCheckedChange = {isSelected ->
                            if (isSelected) {
                                tagList = tagList + tags[index].first
                            } else {
                                tagList = tagList - tags[index].first
                            }
                            onTagChangeEvents(AddEditScreenEvent.AddOrRemoveTag(tagList))
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun TagScreenPreview() {
    ResearchHubTheme {
        TagScreen()
    }
}