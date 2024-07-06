package com.atech.research.ui.screens.login

import android.content.SharedPreferences
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.use_cases.AuthUseCases
import com.atech.core.utils.PrefKeys
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val authUseCases: AuthUseCases,
    private val pref: SharedPreferences,
    private val fcm: FirebaseMessaging
) : ViewModel() {

    private val _logInState = mutableStateOf(LogInState())
    val logInState: State<LogInState> get() = _logInState

    init {
        authUseCases.isUserLoggedInUseCase()?.let {
            _logInState.value = LogInState(uId = it)
        }
    }

    private fun logIn(uid: String) = viewModelScope.launch {
        val localToken = try {
            fcm.token.await()
        } catch (e: Exception) {
            _logInState.value =
                LogInState(errorMessage = e.message)
            ""
        }
        authUseCases.logInWithGoogleStudent(
            uid = uid,
            fcmToken = localToken
        ) { state ->
            when (state) {
                is com.atech.core.utils.State.Error -> _logInState.value =
                    LogInState(errorMessage = state.exception.message)

                com.atech.core.utils.State.Loading -> {}
                is com.atech.core.utils.State.Success -> {
                    _logInState.value = LogInState(uId = state.data)
                }
            }
        }
    }

    fun onEvent(event: LogInScreenEvents) {
        when (event) {
            is LogInScreenEvents.OnSignInResult -> _logInState.value = event.state
            is LogInScreenEvents.TriggerAuth -> logIn(event.token)
            LogInScreenEvents.OnSkipClick -> pref.edit().apply {
                putBoolean(PrefKeys.IS_LOGIN_SKIP.value, true)
            }.apply()

            LogInScreenEvents.PreformLogOutOnError -> authUseCases.signOut {}
        }
    }
}