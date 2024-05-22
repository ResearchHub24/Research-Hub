package com.atech.student.ui.question

sealed interface QuestionStateEvent {
    data class ValidateInput(
        val answers: List<String>,
        val onComplete: (String?) -> Unit = {}
    ) : QuestionStateEvent

    data class PublishApplication(val onComplete: (String?) -> Unit = {}) : QuestionStateEvent
}