package com.atech.student.ui.research.main

import com.atech.core.model.ResearchModel

sealed interface ResearchScreenEvents {
    data class OnItemClick(val model: ResearchModel) : ResearchScreenEvents
    data object ResetClickItem : ResearchScreenEvents
    data class OnAddToWishList(val model: ResearchModel, val isAdded: Boolean = true) :
        ResearchScreenEvents

    data class SetDataFromArgs(val key: String) : ResearchScreenEvents
}