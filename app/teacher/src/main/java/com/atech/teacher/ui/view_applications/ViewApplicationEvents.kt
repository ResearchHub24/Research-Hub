package com.atech.teacher.ui.view_applications

import com.atech.core.model.Action

sealed interface ViewApplicationEvents {
    data class SetKeyFromArgs(val string: String) : ViewApplicationEvents
    data class SelectUserAction(
        val action: Action,
        val ui: String,
        val onComplete: (String?) -> Unit
    ) :
        ViewApplicationEvents
}