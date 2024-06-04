package com.atech.teacher.ui.research

sealed interface VerifyScreenEvents {
    data class OnPassWordChange(val passwordPair: Pair<String, String>) : VerifyScreenEvents
    data object SetPassword : VerifyScreenEvents
}