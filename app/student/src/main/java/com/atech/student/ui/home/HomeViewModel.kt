package com.atech.student.ui.home

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.core.model.UserModel
import com.atech.core.use_cases.AuthUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val authUseCases: AuthUseCases
) : ViewModel() {
    private val _userModel = mutableStateOf(
        if (authUseCases.isUserLoggedInUseCase() != null) authUseCases.getUserDetailsUseFromAuthCase()
        else null
    )
    val userModel: State<UserModel?> get() = _userModel

}