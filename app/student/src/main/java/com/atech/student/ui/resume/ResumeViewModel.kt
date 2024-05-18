package com.atech.student.ui.resume

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.core.model.DUMMY_EDUCATION_DETAILS
import com.atech.core.use_cases.AuthUseCases
import com.atech.core.utils.toJSON
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResumeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    private val _resumeState =
        mutableStateOf(ResumeState())
    val resumeState: State<ResumeState> get() = _resumeState

    init {
        authUseCases.getUserDetailsUseFromAuthCase()?.let {
            _resumeState.value = _resumeState.value.copy(
                userData = it.copy(
                    phone = "+91 1234567890",
                    educationDetails = toJSON(DUMMY_EDUCATION_DETAILS)
                )
            )
        }
    }
}