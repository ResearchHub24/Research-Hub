package com.atech.teacher.ui.add.compose

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.TagModel
import com.atech.core.utils.fromJsonList
import com.atech.teacher.common.MarkdownEditor
import com.atech.teacher.navigation.ResearchRoutes
import com.atech.teacher.navigation.ViewMarkdownArgs
import com.atech.teacher.ui.add.AddEditScreenEvent
import com.atech.ui_common.R
import com.atech.ui_common.common.AppAlertDialog
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.BottomPadding
import com.atech.ui_common.common.DisplayCard
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.toast
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    title: String,
    description: String,
    tags: String,
    question: String,
    onEvent: (AddEditScreenEvent) -> Unit = {},
) {
    val topAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val context = LocalContext.current
    var isConfirmDialogVisible by remember { mutableStateOf(false) }
    BackHandler {
        onEvent.invoke(AddEditScreenEvent.ResetValues)
        navHostController.navigateUp()
    }
    MainContainer(modifier = modifier,
        scrollBehavior = topAppBarScrollBehavior,
        title = if (title.isEmpty()) stringResource(R.string.add_research)
        else stringResource(R.string.edit_research),
        onNavigationClick = {
            onEvent.invoke(AddEditScreenEvent.ResetValues)
            navHostController.navigateUp()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .nestedScroll(topAppBarScrollBehavior.nestedScrollConnection)
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
        ) {
            EditText(modifier = Modifier.fillMaxWidth(),
                value = title,
                placeholder = stringResource(R.string.title),
                supportingMessage = stringResource(R.string.title_des),
                onValueChange = { value ->
                    onEvent.invoke(AddEditScreenEvent.OnTitleChange(value))
                },
                clearIconClick = {
                    onEvent.invoke(AddEditScreenEvent.OnTitleChange(""))
                })
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.spacing.medium
                    )
            )
            MarkdownEditor(value = description, onValueChange = {
                onEvent(AddEditScreenEvent.OnDescriptionChange(it))
            }, viewInMarkdownClick = {
                navHostController.navigate(
                    ViewMarkdownArgs(description)
                )
            })
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.spacing.medium
                    )
            )
            Text(
                text = "Tags",
                style = MaterialTheme.typography.labelSmall,
            )
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.spacing.medium),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
            ) {
                fromJsonList<TagModel>(
                    tags.replace("created_by", "createdBy")
                ).forEach {
                    InputChip(selected = true, label = {
                        Text(it.name)
                    }, onClick = {

                    })
                }
                AssistChip(leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.Add, contentDescription = null
                    )
                }, onClick = {
                    navHostController.navigate(
                        ResearchRoutes.AddTagsScreen.route
                    )
                }, label = {
                    Text(stringResource(R.string.add_tag))
                })
            }
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.spacing.medium
                    )
            )
            Text(
                text = "Questions",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(bottom = MaterialTheme.spacing.small)
            )
            ApplyButton(
                stringResource(R.string.add_or_remove_question),
                horizontalPadding = MaterialTheme.spacing.default
            ) {
                navHostController.navigate(
                    ResearchRoutes.AddQuestionScreen.route
                )
            }
            val questions = fromJsonList<String>(question)
            if (questions.isNotEmpty()) Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            questions.forEach {
                QuestionItem(
                    question = it, enable = false
                )
            }
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            AnimatedVisibility(
                title.isEmpty() || description.isEmpty() || tags.isEmpty() || question.isEmpty()
            ) {
                Column {
                    DisplayCard(
                        modifier = Modifier.fillMaxSize(),
                        border = BorderStroke(
                            width = CardDefaults.outlinedCardBorder().width,
                            color = MaterialTheme.colorScheme.inversePrimary
                        ),
                    ) {
                        val warningMessage = buildAnnotatedString {
                            withStyle(
                                style = SpanStyle(
                                    color = MaterialTheme.colorScheme.primary,
                                    fontSize = MaterialTheme.typography.titleMedium.fontSize
                                )
                            ) {
                                append("Warning")
                                append("\n\n")
                            }
                            if (title.isEmpty()) append("❌ Title\n") else append("✅ Title\n")
                            if (description.isEmpty()) append("❌ Description\n") else append("✅ Description\n")
                            if (tags.isEmpty()) append("❌ Tags\n") else append("✅ Tags\n")
                            if (question.isEmpty()) append("❌ Questions") else append("✅ Questions")
                        }
                        Text(
                            text = warningMessage,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .padding(MaterialTheme.spacing.medium)
                                .fillMaxWidth(),
                            textAlign = TextAlign.Start
                        )
                    }
                    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                }
            }
            AnimatedVisibility(isConfirmDialogVisible) {
                AppAlertDialog(dialogTitle = stringResource(R.string.confirmation),
                    dialogText = stringResource(R.string.save_changes),
                    icon = Icons.Outlined.PostAdd,
                    onDismissRequest = {
                        isConfirmDialogVisible = false
                    },
                    onConfirmation = {
                        onEvent(AddEditScreenEvent.SaveResearch { message ->
                            if (message != null) {
                                toast(context, message)
                                return@SaveResearch
                            }
                            toast(context, context.getString(R.string.saved))
                            isConfirmDialogVisible = false
                            navHostController.navigateUp()
                        })
                    })
            }
            ApplyButton(
                text = stringResource(R.string.save),
                enable = title.isNotEmpty() && description.isNotEmpty() && tags.isNotBlank() && question.isNotBlank(),
                horizontalPadding = MaterialTheme.spacing.default
            ) {
                isConfirmDialogVisible = true
            }
            BottomPadding()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditScreenPreview() {
    ResearchHubTheme {
        AddEditScreen(
            tags = "", title = "", description = "", question = ""
        )
    }
}