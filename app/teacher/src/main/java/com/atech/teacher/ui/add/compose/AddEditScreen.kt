package com.atech.teacher.ui.add.compose

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.atech.teacher.R
import com.atech.teacher.navigation.AddEditScreenArgs
import com.atech.teacher.navigation.TeacherScreenRoutes
import com.atech.teacher.navigation.replaceNA
import com.atech.teacher.ui.add.AddEditScreenEvent
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.EditText
import com.atech.ui_common.common.MainContainer
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing
import com.mohamedrejeb.richeditor.model.rememberRichTextState
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditor
import com.mohamedrejeb.richeditor.ui.material3.RichTextEditorDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditScreen(
    modifier: Modifier = Modifier,
    navHostController: NavHostController = rememberNavController(),
    args: AddEditScreenArgs,
    state: AddEditScreenArgs,
    onEvent: (AddEditScreenEvent) -> Unit = {}
) {
    val richTextState = rememberRichTextState()
    val titleSize = MaterialTheme.typography.displaySmall.fontSize
    val subtitleSize = MaterialTheme.typography.titleLarge.fontSize
    LaunchedEffect(args) {
        args.replaceNA().apply {
            onEvent.invoke(AddEditScreenEvent.SetArgs(this))
            richTextState.setMarkdown(title)
        }
    }
    MainContainer(modifier = modifier,
        title = if (state.key == null) "Add Research" else "Edit Research",
        onNavigationClick = {
            navHostController.popBackStack()
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(paddingValues)
                .padding(MaterialTheme.spacing.medium)
        ) {
            EditText(modifier = Modifier.fillMaxWidth(),
                value = state.title,
                placeholder = "Title",
                supportingMessage = "Title of the research",
                onValueChange = { value ->
                    onEvent.invoke(AddEditScreenEvent.OnTitleChange(value))
                }
            )
            EditorControls(
                modifier = Modifier,
                state = richTextState,
                onBoldClick = {
                    richTextState.toggleSpanStyle(SpanStyle(fontWeight = FontWeight.Bold))
                },
                onItalicClick = {
                    richTextState.toggleSpanStyle(SpanStyle(fontStyle = FontStyle.Italic))
                },
                onUnderlineClick = {
                    richTextState.toggleSpanStyle(SpanStyle(textDecoration = TextDecoration.Underline))
                },
                onTitleClick = {
                    richTextState.toggleSpanStyle(SpanStyle(fontSize = titleSize))
                },
                onSubtitleClick = {
                    richTextState.toggleSpanStyle(SpanStyle(fontSize = subtitleSize))
                },
                onTextColorClick = {
                    richTextState.toggleSpanStyle(SpanStyle(color = Color.Red))
                },
                onStartAlignClick = {
                    richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Start))
                },
                onEndAlignClick = {
                    richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.End))
                },
                onCenterAlignClick = {
                    richTextState.toggleParagraphStyle(ParagraphStyle(textAlign = TextAlign.Center))
                },
            )
            RichTextEditor(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(
                        minHeight = 200.dp
                    ),
                state = richTextState,
                colors = RichTextEditorDefaults.outlinedRichTextEditorColors(
                    cursorColor = MaterialTheme.colorScheme.primary,
                    containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),

                    ),
                placeholder = {
                    Text("Description")
                },
            )
            Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
            ApplyButton(
                text = stringResource(R.string.add_tag)
            ) {
                navHostController.navigate(
                    TeacherScreenRoutes.TagScreen.route
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AddEditScreenPreview() {
    ResearchHubTheme {
        AddEditScreen(
            state = AddEditScreenArgs(key = null).replaceNA(),
            args = AddEditScreenArgs(key = null).replaceNA()
        )
    }
}