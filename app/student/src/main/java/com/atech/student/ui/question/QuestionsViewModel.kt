package com.atech.student.ui.question

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.core.utils.fromJsonList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(

) : ViewModel() {
    private val _questionsState = mutableStateOf(QuestionState())
    val questionsState: State<QuestionState> get() = _questionsState

    fun setKeyAndQuestion(
        question: String,
        key: String
    ) {
        _questionsState.value = _questionsState
            .value.copy(
                questionList = fromJsonList<String>(question)
                    .map { q ->
                        QuestionModel(
                            question = q,
                            answer = ""
                        )
                    }
            )
    }
}