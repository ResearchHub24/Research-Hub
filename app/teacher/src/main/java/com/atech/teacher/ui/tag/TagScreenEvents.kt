package com.atech.teacher.ui.tag

import com.atech.core.model.TagModel


sealed interface TagScreenEvents {
    data class OnCreateNewTag(
        val tagModel: TagModel,
        val onSuccess : () -> Unit
    ) : TagScreenEvents

    data class OnDeleteTagClick(
        val tagModel: TagModel
    ) : TagScreenEvents
}