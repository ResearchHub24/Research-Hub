package com.atech.student.ui.faculties

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.retrofit.FacultyModel
import com.atech.core.use_cases.RetrofitUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FacultiesViewModel @Inject constructor(
    private val useCases: RetrofitUseCases
) : ViewModel() {
    private val _faculties = mutableStateOf<List<FacultyModel>>(emptyList())
    val faculties: State<List<FacultyModel>> get() = _faculties

    init {
        viewModelScope.launch {
            useCases.getFacultiesUseCase().let {
                _faculties.value = it
            }
        }
    }
}