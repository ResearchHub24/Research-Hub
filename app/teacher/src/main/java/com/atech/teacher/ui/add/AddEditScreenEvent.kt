package com.atech.teacher.ui.add

import com.atech.teacher.navigation.AddEditScreenArgs

sealed interface AddEditScreenEvent {
    data class SetArgs(val args: AddEditScreenArgs) : AddEditScreenEvent
    data class OnTitleChange(val title: String) : AddEditScreenEvent
    data class OnDescriptionChange(val description: String) : AddEditScreenEvent
    data class OnCreatedChange(val created: Long) : AddEditScreenEvent
    data class OnDeadLineChange(val deadLine: Long) : AddEditScreenEvent
    data class OnTagsChange(val tags: String) : AddEditScreenEvent
    data class OnQuestionsChange(val questions: String) : AddEditScreenEvent
}