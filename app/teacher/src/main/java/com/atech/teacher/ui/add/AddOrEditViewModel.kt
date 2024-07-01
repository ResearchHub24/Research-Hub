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

    internal fun onEvent(
        event: AddEditScreenEvent
    ) {
        when (event) {
            is AddEditScreenEvent.OnCreatedChange -> _state.value =
                _state.value.copy(created = event.created)

            is AddEditScreenEvent.OnDeadLineChange -> _state.value =
                _state.value.copy(deadLine = event.deadLine)

            is AddEditScreenEvent.OnDescriptionChange -> _description.value = event.description

            is AddEditScreenEvent.OnQuestionsChange -> _state.value =
                _state.value.copy(questions = event.questions)

            is AddEditScreenEvent.OnTitleChange -> _title.value = event.title

            is AddEditScreenEvent.SetArgs -> {
//                Check if the args are same during recomposition of the screen
                if (_state.value areEqual event.args.replaceNA()) {
                    return
                }
                _state.value = event.args.replaceNA()
                _title.value = state.value.title
                _tags.value = state.value.tags
                _description.value = state.value.description
            }

            is AddEditScreenEvent.AddOrRemoveTag -> {
                _tags.value = tagsToJson(event.tags)
            }

            is AddEditScreenEvent.SaveResearch -> {
                useCases.saveResearch.invoke(
                    _state.value.copy(
                        title = title.value, description = description.value, tags = tags.value
                    ).toResearchModel()
                ) {
                    event.onComplete(it?.message)
                }
            }

            AddEditScreenEvent.ResetValues -> {
                _title.value = ""
                _description.value = ""
                _tags.value = ""
            }
        }
    }

    internal fun getTagsFromString(json: String) = fromJsonList<TagModel>(json)

    private fun tagsToJson(tags: List<TagModel>) = toJSON(tags)


}