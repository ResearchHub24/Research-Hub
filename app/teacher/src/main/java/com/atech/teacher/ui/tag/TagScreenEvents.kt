package com.atech.teacher.ui.tag

import com.atech.core.model.TagModel


sealed interface TagScreenEvents {
    data class CreateNewTag(
        val tagModel: TagModel,
        val onSuccess: () -> Unit
    ) : TagScreenEvents
}