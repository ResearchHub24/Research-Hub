/*
 *  Created by aiyu
 *  Copyright (c) 2021 . All rights reserved.
 *  BIT App
 *
 */

package com.atech.ui_common.common

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.CodeBlockStyle
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.RichText
import com.halilibo.richtext.ui.string.RichTextStringStyle

@Composable
fun MarkDown(
    modifier: Modifier = Modifier,
    markDown: String,
    codeBlockBackBackground: Color = Color.Gray,
    linkTextColor: Color = MaterialTheme.colorScheme.primary,
    wrapWord: Boolean = true
) {
    RichText(
        modifier = modifier,
        style = RichTextStyle.Default
            .copy(
                codeBlockStyle = CodeBlockStyle.Default
                    .copy(
                        modifier = Modifier
                            .background(color = Color.Black, shape = MaterialTheme.shapes.small),
                        textStyle = TextStyle(
                            fontFamily = FontFamily.Monospace,
                            fontSize = MaterialTheme.typography.bodySmall.fontSize,
                            color = Color.White
                        ),
                        wordWrap = wrapWord,
                        padding = 8.sp
                    ),
                stringStyle = RichTextStringStyle.Default.copy(
                    linkStyle = SpanStyle(
                        textDecoration = TextDecoration.Underline,
                        color = linkTextColor
                    ),
                    codeStyle = SpanStyle(
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Medium,
                        background = codeBlockBackBackground
                    ),
                )
            )
    ) {
        Markdown(content = markDown)
    }
}