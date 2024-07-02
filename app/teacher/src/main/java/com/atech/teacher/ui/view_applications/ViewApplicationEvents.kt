package com.atech.teacher.ui.view_applications

sealed interface ViewApplicationEvents {
    data class SetKeyFromArgs(val string: String) : ViewApplicationEvents
}