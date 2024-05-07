package com.atech.student.ui.research

import androidx.lifecycle.ViewModel
import com.atech.core.use_cases.ResearchUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResearchViewModel @Inject constructor(
    useCases: ResearchUseCases
) : ViewModel() {
    val research = useCases.getAllResearchUseCase()
}
