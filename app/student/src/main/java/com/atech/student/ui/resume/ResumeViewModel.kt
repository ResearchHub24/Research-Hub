package com.atech.student.ui.resume

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.use_cases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResumeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    private val _resumeState =
        mutableStateOf(ResumeState())
    val resumeState: State<ResumeState> get() = _resumeState

    private val _addScreenState = mutableStateOf(AddScreenState())
    val addScreenState: State<AddScreenState> get() = _addScreenState

    init {
        updateUserDetails()
    }

    private fun updateUserDetails() {
        viewModelScope.launch {
            authUseCases.getUserDetailsUseFromAuthCase(
                fromDatabase = true
            )?.let {
                _resumeState.value = _resumeState.value.copy(
                    userData = it
                )
            }
        }
    }

    fun onEvent(event: ResumeScreenEvents) {
        when (event) {
            ResumeScreenEvents.OnPersonalDetailsClick -> {
                _addScreenState.value = AddScreenState(
                    screenType = AddEditScreenType.DETAILS,
                    personalDetails = Triple(
                        first = _resumeState.value.userData.name,
                        second = _resumeState.value.userData.email,
                        third = _resumeState.value.userData.phone ?: ""
                    )
                )
            }

            is ResumeScreenEvents.OnPersonalDataEdit -> {
                _addScreenState.value = addScreenState.value.copy(
                    personalDetails = Triple(
                        first = event.name,
                        second = _resumeState.value.userData.email,
                        third = event.phone
                    )
                )
            }

            ResumeScreenEvents.UpdateUserDetails -> updateUserDetails()

            ResumeScreenEvents.OnAddEditEducationClick -> {
                _addScreenState.value = AddScreenState(
                    screenType = AddEditScreenType.EDUCATION,
                )
            }

        }
    }
}