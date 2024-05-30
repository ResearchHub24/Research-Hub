package com.atech.research.ui.screens.verify

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class VerifyViewModel @Inject constructor(

) : ViewModel() {
    private val _verifyScreenState = mutableStateOf(VerifyScreenState())
    val verifyScreenState: State<VerifyScreenState> get() = _verifyScreenState
    fun onEvent(event: VerifyScreenEvents) {
        when (event) {
            is VerifyScreenEvents.OnPassWordChange -> {
                _verifyScreenState.value =
                    _verifyScreenState.value.copy(passwordPair = event.passwordPair)
            }
        }
    }
}