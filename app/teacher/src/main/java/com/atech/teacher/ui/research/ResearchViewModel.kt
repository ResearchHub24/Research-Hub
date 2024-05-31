package com.atech.teacher.ui.research

import androidx.lifecycle.ViewModel
import com.atech.core.use_cases.TeacherAuthUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResearchViewModel @Inject constructor(
    useCase: TeacherAuthUserCase
) : ViewModel() {
    val research = useCase.getAllPosted.invoke()
}