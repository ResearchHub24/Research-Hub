package com.atech.student.ui.research.main

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.core.model.ResearchModel
import com.atech.core.use_cases.FireStoreUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResearchViewModel @Inject constructor(
    useCases: FireStoreUseCases
) : ViewModel() {
    val research = useCases.getAllResearchUseCase()

    private val _clickItem = mutableStateOf<ResearchModel?>(null)
    val clickItem: State<ResearchModel?> get() = _clickItem

    fun onEvent(event: ResearchScreenEvents) {
        when (event) {
            is ResearchScreenEvents.OnItemClick -> {
                Log.d("AAA", "onEvent: ${event.model}")
                _clickItem.value = event.model
            }

            ResearchScreenEvents.ResetClickItem -> _clickItem.value = null
        }
    }
}
