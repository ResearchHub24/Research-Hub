package com.atech.teacher.ui.verify

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.use_cases.TeacherAuthUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VerifyScreenViewModel @Inject constructor(
    private val useCase: TeacherAuthUserCase
) : ViewModel() {

    private val _verifyScreenState = mutableStateOf(VerifyScreenState())
    val verifyScreenState: State<VerifyScreenState> get() = _verifyScreenState

    init {
        loadData()
    }

    fun onVerifyScreenEvent(event: VerifyScreenEvents) {
        when (event) {
            is VerifyScreenEvents.OnPassWordChange -> {
                _verifyScreenState.value =
                    _verifyScreenState.value.copy(passwordPair = event.passwordPair)
            }

            VerifyScreenEvents.SetPassword -> {
                viewModelScope.launch {
                    useCase.saveData.savePassword(_verifyScreenState.value.passwordPair.first) {
                        if (it != null) {
                            _verifyScreenState.value =
                                _verifyScreenState.value.copy(errorMessage = it.message)
                            return@savePassword
                        }
                        _verifyScreenState.value = _verifyScreenState.value.copy(userData = null)
                        loadData()
                    }
                }
            }
        }
    }

    private fun loadData() {
        viewModelScope.launch {
            useCase.getTeacherData.invoke()?.let {
                _verifyScreenState.value = _verifyScreenState.value.copy(userData = it)
            }
        }
    }

}