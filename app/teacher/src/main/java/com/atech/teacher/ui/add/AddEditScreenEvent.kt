package com.atech.teacher.ui.add

import com.atech.core.model.TagModel

sealed interface AddEditScreenEvent {
    data class OnTitleChange(val title: String) : AddEditScreenEvent
    data class OnDescriptionChange(val description: String) : AddEditScreenEvent
    data class OnCreatedChange(val created: Long) : AddEditScreenEvent
    data class OnDeadLineChange(val deadLine: Long) : AddEditScreenEvent
    data class OnQuestionsChange(val questions: String) : AddEditScreenEvent
    data class AddOrRemoveTag(val tags: List<TagModel>) : AddEditScreenEvent
    data class SaveResearch(val onComplete: (String?) -> Unit) :
        AddEditScreenEvent
}