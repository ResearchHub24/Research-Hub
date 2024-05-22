package com.atech.student.ui.question

data class QuestionModel(
    val question: String,
    val answer: String
)

data class QuestionState(
    val questionList: List<QuestionModel> = emptyList()
)