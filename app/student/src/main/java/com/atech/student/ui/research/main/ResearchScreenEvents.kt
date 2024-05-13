package com.atech.student.ui.research.main

import com.atech.core.model.ResearchModel

sealed interface ResearchScreenEvents {
    data class OnItemClick(val model: ResearchModel) : ResearchScreenEvents
    data object ResetClickItem : ResearchScreenEvents
}