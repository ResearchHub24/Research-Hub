package com.atech.teacher.ui.add.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.atech.core.utils.fromJsonList
import com.atech.core.utils.toJSON
import com.atech.teacher.ui.add.AddEditScreenEvent
import com.atech.ui_common.R
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.EditTextEnhance
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.common.toast
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddQuestionScreen(
    modifier: Modifier = Modifier,
    navController: NavController = rememberNavController(),
    questionsJson: String,
    onEvent: (AddEditScreenEvent) -> Unit = {}
) {
    val context = LocalContext.current
    var question by rememberSaveable { mutableStateOf("") }
    var questions by rememberSaveable { mutableStateOf(fromJsonList<String>(questionsJson)) }
    MainContainer(modifier = modifier,
        title = stringResource(R.string.add_or_remove_question),
        onNavigationClick = {
            navController.navigateUp()
        },
        actions = {
            IconButton(onClick = {
                if (questions.isEmpty()) {
                    toast(context, "Please add at least one question")
                    return@IconButton
                }
                onEvent.invoke(AddEditScreenEvent.OnQuestionsChange(toJSON(questions)))
                navController.navigateUp()
            }) {
                Icon(
                    imageVector = Icons.Outlined.Done,
                    contentDescription = stringResource(R.string.apply)
                )
            }
        }) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .padding(MaterialTheme.spacing.medium)
                .fillMaxSize(),
            contentPadding = paddingValues,
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            item(
                key = "Add"
            ) {
                Column {
                    EditTextEnhance(
                        modifier = Modifier.fillMaxWidth(),
                        value = question,
                        onValueChange = { question = it },
                        placeholder = stringResource(R.string.question),
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Sentences,
                            imeAction = ImeAction.Done
                        )
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
                    AnimatedVisibility(question.isNotBlank()) {
                        ApplyButton(
                            text = "Add", horizontalPadding = MaterialTheme.spacing.default
                        ) {
                            questions += question
                            question = ""
                        }
                    }
                }
            }
            questions = questions.filter { it.isNotBlank() } // filter out empty string
            itemsIndexed(questions) { index, item ->
                QuestionItem(question = item, onValueChange = {
                    questions = questions.toMutableList().apply {
                        set(index, it)
                    }
                }, onClearClick = {
                    questions = questions.toMutableList().apply {
                        removeAt(index)
                    }
                })
            }
        }
    }
}

@Composable
fun QuestionItem(
    modifier: Modifier = Modifier,
    question: String,
    enable: Boolean = true,
    onValueChange: (String) -> Unit = {},
    onClearClick: () -> Unit = {}
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            EditTextEnhance(
                enable = enable,
                modifier = Modifier.fillMaxWidth(),
                value = question,
                onValueChange = onValueChange,
                placeholder = stringResource(R.string.question),
                clearIconClick = onClearClick
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddQuestionScreenPreview() {
    ResearchHubTheme {
        AddQuestionScreen(
            questionsJson = ""
        )
//        QuestionItem(
//            question = "This is Question"
//        )
    }
}