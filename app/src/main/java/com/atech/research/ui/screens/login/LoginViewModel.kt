package com.atech.research.ui.screens.login

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(

) : ViewModel() {

    private val _logInState = mutableStateOf(LogInState())
    val logInState: State<LogInState> get() = _logInState
    private val _ui = mutableStateOf("")
    val ui: State<String> get() = _ui

    fun onEvent(event: LogInScreenEvents) {
        when (event) {
            is LogInScreenEvents.OnSignInResult -> _logInState.value = event.state
            LogInScreenEvents.SetUID -> _ui.value = ""
        }
    }
}