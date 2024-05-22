package com.atech.student.ui.question

import androidx.compose.runtime.Composable

sealed interface QuestionStateEvent {
    data class ValidateInput(
        val answers: List<String>,
        val onComplete:  (String?) -> Unit = {}
    ) : QuestionStateEvent
}