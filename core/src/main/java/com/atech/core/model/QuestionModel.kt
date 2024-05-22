package com.atech.core.model

import androidx.annotation.Keep

@Keep
data class QuestionModel(
    val question: String? = null,
    val answer: String? = null
)