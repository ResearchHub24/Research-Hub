package com.atech.student.ui.question

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.model.QuestionModel
import com.atech.core.model.ResearchPublishModel
import com.atech.core.use_cases.AuthUseCases
import com.atech.core.utils.fromJsonList
import com.atech.core.utils.toJSON
import com.atech.student.navigation.QuestionScreenArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuestionsViewModel @Inject constructor(
    private val authCase: AuthUseCases
) : ViewModel() {
    private val _questionsState = mutableStateOf(emptyList<String>())
    val questionsState: State<List<String>> get() = _questionsState
    private lateinit var args: QuestionScreenArgs
    private var answerList = emptyList<QuestionModel>()

    fun setArgs(
        args: QuestionScreenArgs
    ) {
        this.args = args
        _questionsState.value = fromJsonList<String>(args.question)
    }

    fun onEven(event: QuestionStateEvent) {
        when (event) {
            is QuestionStateEvent.ValidateInput -> {
                if (args.key.isEmpty()) {
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

                this.answerList =
                    _questionsState.value.zip(event.answers).toMap().map { (question, answer) ->
                        QuestionModel(
                            question = question, answer = answer
                        )
                    }
                event.onComplete.invoke(null)
            }

            is QuestionStateEvent.PublishApplication -> viewModelScope.launch {
                authCase.publishApplication.invoke(
                    key = args.key,
                    model = ResearchPublishModel(
                        studentName = args.userName,
                        studentEmail = args.userEmail,
                        studentPhoneNumber = args.userPhone,
                        answers = toJSON(answerList),
                    ),
                    filledForm = if(args.filledForm == "[]")"" else args.filledForm,
                ) { exception ->
                    event.onComplete.invoke(exception?.message)
                }
            }
        }
    }
}