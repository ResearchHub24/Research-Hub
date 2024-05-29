package com.atech.student.ui.faculties

import androidx.lifecycle.ViewModel
import com.atech.core.use_cases.GetAllFaculties
import com.atech.core.use_cases.RetrofitUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FacultiesViewModel @Inject constructor(
    useCases: RetrofitUseCases,
    getAllFaculties: GetAllFaculties
) : ViewModel() {
    val faculties = useCases.getAndSaveFacultiesUseCase()
    val registerFaculties = getAllFaculties.invoke()

}