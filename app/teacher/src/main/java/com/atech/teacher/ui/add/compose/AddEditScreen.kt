package com.atech.teacher.ui.add.compose

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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.core.model.TagModel
import com.atech.core.utils.fromJsonList
import com.atech.teacher.common.MarkdownEditor
import com.atech.teacher.navigation.ResearchRoutes
import com.atech.teacher.ui.add.AddEditScreenEvent
import com.atech.ui_common.R
import com.atech.ui_common.common.ApplyButton
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
    onEvent: (AddEditScreenEvent) -> Unit = {},
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_DESTROY -> {
                    onEvent(AddEditScreenEvent.ResetValues)
                }

                else -> Unit
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }
    MainContainer(modifier = modifier,
        title = if (title.isEmpty()) "Add Research" else "Edit Research",
        onNavigationClick = {
            navHostController.navigateUp()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .imePadding()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
        ) {
            EditText(modifier = Modifier.fillMaxWidth(),
                value = title,
                placeholder = "Title",
                supportingMessage = "Title of the research",
                onValueChange = { value ->
                    onEvent.invoke(AddEditScreenEvent.OnTitleChange(value))
                })
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        vertical = MaterialTheme.spacing.medium
                    )
            )
            MarkdownEditor(
                value = description,
                onValueChange = {
                    onEvent(AddEditScreenEvent.OnDescriptionChange(it))
                }
            )
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
            ApplyButton(
                text = "Save"/*stringResource(R.string.save)*/
            ) {
                onEvent(AddEditScreenEvent.SaveResearch { message ->
                    if (message != null) {
                        toast(context, message)
                        return@SaveResearch
                    }
                    toast(context, "Saved")
                    navHostController.navigateUp()
                })
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditScreenPreview() {
    ResearchHubTheme {
        AddEditScreen(
            tags = "", title = "", description = ""
        )
    }
}