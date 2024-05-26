package com.atech.student.ui.faculties

import androidx.lifecycle.ViewModel
import com.atech.core.use_cases.RetrofitUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FacultiesViewModel @Inject constructor(
    private val useCases: RetrofitUseCases
) : ViewModel() {
    val faculties = useCases.getAndSaveFacultiesUseCase()
}