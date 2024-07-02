package com.atech.teacher.ui.view_applications

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.model.ResearchPublishModel
import com.atech.core.use_cases.TeacherAuthUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ViewApplicationViewModel @Inject constructor(
    private val useCase: TeacherAuthUserCase
) : ViewModel() {
    private val _key = mutableStateOf("")


    private val _submittedForms = mutableStateOf<List<ResearchPublishModel>>(emptyList())
    val submittedForms: State<List<ResearchPublishModel>> get() = _submittedForms

    private var job: Job? = null
    fun onEvent(event: ViewApplicationEvents) {
        when (event) {
            is ViewApplicationEvents.SetKeyFromArgs -> {
                _key.value = event.string
                fetchData()
            }
        }
    }

    private fun fetchData() {
        job?.cancel()
        job = useCase
            .getAllSubmittedForm
            .invoke(_key.value)
            .onEach {
                _submittedForms.value = it
            }
            .launchIn(viewModelScope)
    }
}