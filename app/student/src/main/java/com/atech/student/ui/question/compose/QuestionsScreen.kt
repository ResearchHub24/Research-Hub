package com.atech.student.ui.question.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Mail
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.student.ui.question.QuestionStateEvent
import com.atech.student.ui.resume.compose.ApplyButton
import com.atech.ui_common.R
import com.atech.ui_common.common.AppAlertDialog
import com.atech.ui_common.common.DisplayCard
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    state: List<String> = emptyList(),
    onEvent: (QuestionStateEvent) -> Unit = {}
) {
    MainContainer(title = "Questions", modifier = modifier, onNavigationClick = {
        navController.popBackStack()
    }) { paddingValues ->
        var isDialogVisible by rememberSaveable { mutableStateOf(false) }
        var answers by remember {
            mutableStateOf(
                List(state.size) { "" }
            )
        }
        var errorMessage by remember {
            mutableStateOf("")
        }
        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .consumeWindowInsets(paddingValues)
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(
                MaterialTheme.spacing.medium
            )
        ) {
            items(state.size) { pos ->
                QuestionItem(
                    question = state[pos],
                    answer = answers[pos]
                ) { answer ->
                    answers = answers.toMutableList().also {
                        it[pos] = answer
                    }
                }
            }
            item(
                key = "submit"
            ) {
                Column {
                    AnimatedVisibility(
                        visible = errorMessage.isNotEmpty()
                    ) {
                        DisplayCard(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = MaterialTheme.spacing.medium),
                            border = BorderStroke(
                                width = CardDefaults.outlinedCardBorder().width,
                                color = MaterialTheme.colorScheme.error
                            ),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.ErrorOutline,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = errorMessage,
                                    modifier = Modifier.padding(MaterialTheme.spacing.medium),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
                    ApplyButton(text = "Submit") {
                        onEvent.invoke(
                            QuestionStateEvent.ValidateInput(answers) { message ->
                                if (message != null) {
                                    errorMessage = message
                                    return@ValidateInput
                                }
                                errorMessage = ""
                                isDialogVisible = true
                            }
                        )
                    }
                    if (isDialogVisible)
                        AppAlertDialog(
                            dialogTitle = "Confirm Submit",
                            dialogText = "Are you sure you want to submit?",
                            icon = Icons.Outlined.Mail,
                            onDismissRequest = {
                                isDialogVisible = false
                            }
                        ) {
                            isDialogVisible = true
//                            TODO: Submit answers
                        }
                }
            }
        }
    }
}

@Composable
fun QuestionItem(
    modifier: Modifier = Modifier,
    question: String,
    answer: String = "",
    onValueChange: (String) -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            text = question,
            style = MaterialTheme.typography.titleMedium
        )
        EditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            value = answer,
            placeholder = "Answer",
            supportingMessage = stringResource(R.string.require),
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences,
                keyboardType = KeyboardType.Text
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun QuestionScreenPreview() {
    ResearchHubTheme {
        QuestionScreen(

        )
    }
}