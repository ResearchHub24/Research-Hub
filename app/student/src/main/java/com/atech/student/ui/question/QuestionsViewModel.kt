package com.atech.student.ui.question

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.core.model.QuestionModel
import com.atech.core.utils.fromJsonList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor() : ViewModel() {
    private val _questionsState = mutableStateOf(emptyList<String>())
    val questionsState: State<List<String>> get() = _questionsState
    private var key: String? = null

    fun setKeyAndQuestion(
        question: String,
        key: String
    ) {
        this.key = key
        _questionsState.value = fromJsonList<String>(question)
    }

    fun onEven(event: QuestionStateEvent) {
        when (event) {
            is QuestionStateEvent.ValidateInput -> {
                if (key == null) {
                    event.onComplete.invoke("SomeThing Went Wrong !! Error: Key is null")
                    return
                }
                if (event.answers.size != _questionsState.value.size) {
                    event.onComplete.invoke("SomeThing Went Wrong !! Error: Answer size is not equal to question size")
                    return
                }
//                check empty answers
                if (event.answers.any { it.isEmpty() }) {
                    event.onComplete.invoke("All answers are required !!")
                    return
                }
//                check at least have 30 words in each answer excluding space
                if (event.answers.any { it.split(" ").size < 20 }) {
                    event.onComplete.invoke("All answers should have at least 20 words !!")
                    return
                }

                val answerList = _questionsState.value.zip(event.answers).toMap()
                    .map { (question, answer) ->
                        QuestionModel(
                            question = question,
                            answer = answer
                        )
                    }
                event.onComplete.invoke(null)
            }
        }
    }
}