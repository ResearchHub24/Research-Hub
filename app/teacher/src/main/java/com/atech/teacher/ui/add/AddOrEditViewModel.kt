package com.atech.teacher.ui.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.core.model.TagModel
import com.atech.core.use_cases.TeacherAuthUserCase
import com.atech.core.utils.fromJsonList
import com.atech.core.utils.toJSON
import com.atech.teacher.navigation.AddEditScreenArgs
import com.atech.teacher.navigation.areEqual
import com.atech.teacher.navigation.replaceNA
import com.atech.teacher.navigation.toResearchModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddOrEditViewModel @Inject constructor(
    private val useCases: TeacherAuthUserCase
) : ViewModel() {

    private val _state = mutableStateOf(AddEditScreenArgs(key = null).replaceNA())
    val state: State<AddEditScreenArgs> get() = _state
    private val _title = mutableStateOf(state.value.title)
    val title: State<String> get() = _title

    private val _description = mutableStateOf(state.value.description)
    val description: State<String> get() = _description

    private val _tags = mutableStateOf(state.value.tags)
    val tags: State<String> get() = _tags

    private val _question = mutableStateOf(state.value.questions)
    val question: State<String> get() = _question

    internal fun onEvent(
        event: AddEditScreenEvent
    ) {
        when (event) {
            is AddEditScreenEvent.OnCreatedChange -> _state.value =
                _state.value.copy(created = event.created)

            is AddEditScreenEvent.OnDeadLineChange -> _state.value =
                _state.value.copy(deadLine = event.deadLine)

            is AddEditScreenEvent.OnDescriptionChange -> _description.value = event.description

            is AddEditScreenEvent.OnQuestionsChange -> _question.value = event.questions

            is AddEditScreenEvent.OnTitleChange -> _title.value = event.title

            is AddEditScreenEvent.SetArgs -> {
//                Check if the args are same during recomposition of the screen
                // to avoid unnecessary recomposition
                if (_state.value areEqual event.args.replaceNA()) {
                    return
                }
                _state.value = event.args.replaceNA()
                _title.value = state.value.title
                _tags.value = state.value.tags
                _description.value = state.value.description
                _question.value = state.value.questions
            }

            is AddEditScreenEvent.AddOrRemoveTag -> {
                _tags.value = tagsToJson(event.tags)
            }

            is AddEditScreenEvent.SaveResearch -> {
                val updatedModel = _state.value.copy(
                    title = title.value,
                    description = description.value,
                    tags = tags.value,
                    questions = question.value
                )
                if (updatedModel == state.value) {
                    event.onComplete.invoke(null)
                    return
                }
                useCases.saveResearch.invoke(
                    updatedModel.toResearchModel()
                ) {
                    event.onComplete(it?.message)
                }
            }

            AddEditScreenEvent.ResetValues -> {
                _state.value = AddEditScreenArgs(key = null).replaceNA()
                _title.value = ""
                _description.value = ""
                _tags.value = ""
                _question.value = ""
            }
        }
    }

    internal fun getTagsFromString(json: String) = fromJsonList<TagModel>(json)

    private fun tagsToJson(tags: List<TagModel>) = toJSON(tags)


}