package com.atech.research.ui.screens.verify

sealed interface VerifyScreenEvents {
    data class OnPassWordChange(val passwordPair: Pair<String, String>) : VerifyScreenEvents
}