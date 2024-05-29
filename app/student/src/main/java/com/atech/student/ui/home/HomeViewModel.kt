package com.atech.student.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.model.StudentUserModel
import com.atech.core.use_cases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    private val _Student_userModel = mutableStateOf(
        null as StudentUserModel?
    )
    val studentUserModel: State<StudentUserModel?> get() = _Student_userModel

    init {
        viewModelScope.launch {
            authUseCases.getUserDetailsUseFromAuthCase()?.let {
                _Student_userModel.value = it
            }
        }
    }
}