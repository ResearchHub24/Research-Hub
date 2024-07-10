package com.atech.teacher.ui.add

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.toRoute
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
    savedState: SavedStateHandle, private val useCases: TeacherAuthUserCase
) : ViewModel() {

    private val _state = mutableStateOf(savedState.toRoute<AddEditScreenArgs>().replaceNA())
    val state: State<AddEditScreenArgs> get() = _state
    private val _title = mutableStateOf(state.value.title)
    val title: State<String> get() = _title
    private val oldData = _state.value

    internal fun onEvent(
        event: AddEditScreenEvent
    ) {
        when (event) {
            is AddEditScreenEvent.OnCreatedChange -> _state.value =
                _state.value.copy(created = event.created)

            is AddEditScreenEvent.OnDeadLineChange -> _state.value =
                _state.value.copy(deadLine = event.deadLine)

            is AddEditScreenEvent.OnDescriptionChange -> _state.value =
                _state.value.copy(description = event.description)

            is AddEditScreenEvent.OnQuestionsChange -> _state.value =
                _state.value.copy(questions = event.questions)

            is AddEditScreenEvent.OnTitleChange -> _state.value =
                _state.value.copy(title = event.title)


            is AddEditScreenEvent.AddOrRemoveTag -> _state.value =
                _state.value.copy(tags = tagsToJson(event.tags))

            is AddEditScreenEvent.SaveResearch -> {
                if (oldData areEqual state.value) {
                    event.onComplete.invoke(null)
                    return
                }
                useCases.saveResearch.invoke(
                    state.value.toResearchModel()
                ) {
                    event.onComplete(it?.message)
                }
            }
        }
    }

    internal fun getTagsFromString(json: String) = fromJsonList<TagModel>(json)

    private fun tagsToJson(tags: List<TagModel>) = toJSON(tags)


}