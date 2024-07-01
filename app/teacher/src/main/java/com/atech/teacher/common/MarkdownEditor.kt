package com.atech.teacher.common

import android.content.res.Configuration.UI_MODE_NIGHT_YES
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material.icons.filled.FormatQuote
import androidx.compose.material.icons.filled.FormatUnderlined
import androidx.compose.material.icons.filled.IntegrationInstructions
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.atech.ui_common.common.ApplyButton
import com.atech.ui_common.common.textFieldColors
import com.atech.ui_common.theme.ResearchHubTheme
import com.atech.ui_common.theme.spacing

@Composable
fun ControlWrapper(
    onClick: () -> Unit,
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(size = 6.dp))
            .clickable {
                onClick()
            }
            .background(
                MaterialTheme.colorScheme.inversePrimary
            )
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(size = 6.dp)
            )
            .padding(all = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        content()
    }
}

fun String.endWithSpace(other: String) = this.run {
    if (this.isBlank())
        this + other
    else {
        if (this.endsWith("")) "$this $other"
        else this + other
    }
}

fun String.endWithNewLine(other: String) = this.run {
    if (this.isBlank())
        this + other
    else {
        if (this.endsWith("\n"))
            this + other
        else "$this\n$other"
    }
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MarkdownEditor(
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
    viewInMarkdownClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(all = MaterialTheme.spacing.medium),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var textFieldValueState by remember {
            mutableStateOf(
                TextFieldValue(
                    text = value,
                    selection = TextRange(value.length)
                )
            )
        }
        val focusRequester = remember { FocusRequester() }
        FlowRow(
            modifier = modifier
                .fillMaxWidth()
                .padding(bottom = MaterialTheme.spacing.large),
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium)
        ) {
            ControlWrapper(
                onClick = {
                    val text =
                        textFieldValueState.text.endWithNewLine("# ")
                    textFieldValueState = TextFieldValue(
                        text = text,
                        selection = TextRange(text.length)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Title,
                    contentDescription = "Title Control",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            ControlWrapper(
                onClick = {
                    val text =
                        textFieldValueState.text.endWithNewLine("- ")
                    textFieldValueState = TextFieldValue(
                        text = text,
                        selection = TextRange(text.length)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.List,
                    contentDescription = "Create List",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            ControlWrapper(
                onClick = {
                    val text = textFieldValueState.text.endWithSpace("****")
                    textFieldValueState = TextFieldValue(
                        text = text,
                        selection = TextRange(text.length - 2)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.FormatBold,
                    contentDescription = "Bold Control",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            ControlWrapper(
                onClick = {
                    val text = textFieldValueState.text.endWithSpace("**")
                    textFieldValueState = TextFieldValue(
                        text = text,
                        selection = TextRange(text.length - 1)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.FormatItalic,
                    contentDescription = "Italic Control",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            ControlWrapper(
                onClick = {
                    val text = textFieldValueState.text.endWithSpace("<u></u>")
                    textFieldValueState = TextFieldValue(
                        text = text,
                        selection = TextRange(text.length - 4)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.FormatUnderlined,
                    contentDescription = "Underline Control",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            ControlWrapper(
                onClick = {
                    val text =
                        textFieldValueState.text.endWithNewLine(">")
                    textFieldValueState = TextFieldValue(
                        text = text,
                        selection = TextRange(text.length)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.FormatQuote,
                    contentDescription = "Quote Text",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            ControlWrapper(
                onClick = {
                    val text =
                        textFieldValueState.text.endWithNewLine("``")
                    textFieldValueState = TextFieldValue(
                        text = text,
                        selection = TextRange(text.length - 1)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Code,
                    contentDescription = "Add Code Highlight",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            ControlWrapper(
                onClick = {
                    val text =
                        textFieldValueState.text.endWithNewLine("``````")
                    textFieldValueState = TextFieldValue(
                        text = text,
                        selection = TextRange(text.length - 3)
                    )
                }
            ) {
                Icon(
                    imageVector = Icons.Default.IntegrationInstructions,
                    contentDescription = "Add Code Block",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .defaultMinSize(
                    minHeight = 200.dp
                ),
            colors = textFieldColors(),
            value = textFieldValueState,
            label = {
                Text("Description")
            },
            placeholder = {
                Text("Description")
            },
            onValueChange = {
                textFieldValueState = it
                onValueChange.invoke(it.text)
            },
            trailingIcon = {
                if (textFieldValueState.text.isNotBlank())
                    IconButton(onClick = {
                        textFieldValueState = TextFieldValue(
                            text = "",
                            selection = TextRange(0)
                        )
                        onValueChange.invoke("")
                    }) {
                        Icon(imageVector = Icons.Outlined.Clear, contentDescription = null)
                    }
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
        AnimatedVisibility(textFieldValueState.text.isNotBlank()) {
            ApplyButton(
                text = "View Markdown",
                horizontalPadding = MaterialTheme.spacing.default,
                action = {
                    viewInMarkdownClick.invoke()
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Preview(showBackground = false, uiMode = UI_MODE_NIGHT_YES)
@Composable
private fun MarkdownEditorPreview() {
    ResearchHubTheme {
        MarkdownEditor(
            value = "Be Real 😉"
        )
    }
}