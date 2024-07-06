package com.atech.teacher.ui.research

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.use_cases.TeacherAuthUserCase
import com.atech.teacher.ui.verify.VerifyScreenEvents
import com.atech.teacher.ui.verify.VerifyScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResearchViewModel @Inject constructor(
    private val useCase: TeacherAuthUserCase
) : ViewModel() {
    val research = useCase.getAllPosted.invoke()

}