package com.atech.teacher.ui.add

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.teacher.navigation.AddEditScreenArgs
import com.atech.teacher.navigation.replaceNA
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AddOrEditViewModel @Inject constructor(

) : ViewModel() {
    private val _state = mutableStateOf(AddEditScreenArgs(key = null).replaceNA())
    val state: State<AddEditScreenArgs> get() = _state

    internal fun onEvent(
        event: AddEditScreenEvent
    ) {
        when (event) {

            is AddEditScreenEvent.OnCreatedChange ->
                _state.value = _state.value.copy(created = event.created)

            is AddEditScreenEvent.OnDeadLineChange ->
                _state.value = _state.value.copy(deadLine = event.deadLine)

            is AddEditScreenEvent.OnDescriptionChange ->
                _state.value = _state.value.copy(description = event.description)

            is AddEditScreenEvent.OnQuestionsChange ->
                _state.value = _state.value.copy(questions = event.questions)

            is AddEditScreenEvent.OnTagsChange ->
                _state.value = _state.value.copy(tags = event.tags)

            is AddEditScreenEvent.OnTitleChange ->
                _state.value = _state.value.copy(title = event.title)

            is AddEditScreenEvent.SetArgs -> _state.value = event.args.replaceNA()
        }
    }
}