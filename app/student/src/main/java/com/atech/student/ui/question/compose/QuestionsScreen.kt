package com.atech.student.ui.question.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.student.ui.question.QuestionState
import com.atech.ui_common.R
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuestionScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    state: QuestionState = QuestionState()
) {
    MainContainer(title = "Questions", modifier = modifier, onNavigationClick = {
        navController.popBackStack()
    }) { paddingValues ->
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
            items(state.questionList) {
                QuestionItem(
                    question = it.question, answer = it.answer
                )
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
            onValueChange = onValueChange
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