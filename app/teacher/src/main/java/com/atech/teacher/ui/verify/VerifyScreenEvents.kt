package com.atech.teacher.ui.verify

sealed interface VerifyScreenEvents {
    data class OnPassWordChange(val passwordPair: Pair<String, String>) : VerifyScreenEvents
    data object SetPassword : VerifyScreenEvents
}