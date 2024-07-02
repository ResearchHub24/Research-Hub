package com.atech.teacher.ui.student_profile

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.model.StudentUserModel
import com.atech.core.use_cases.GetStudentUserDataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StudentProfileViewModel @Inject constructor(
    private val useCase: GetStudentUserDataUseCase
) : ViewModel() {
    private val _uid = mutableStateOf("")

    private val _userModel = mutableStateOf<StudentUserModel?>(StudentUserModel())
    val userModel: State<StudentUserModel?> get() = _userModel

    internal fun setUId(ui: String) {
        _uid.value = ui
        getUserDetails(ui)
    }

    private fun getUserDetails(ui: String) = viewModelScope.launch {
        _userModel.value = useCase.invoke(ui)
    }

}