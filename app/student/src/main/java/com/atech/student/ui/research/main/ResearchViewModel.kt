package com.atech.student.ui.research.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.model.ResearchModel
import com.atech.core.use_cases.FireStoreUseCases
import com.atech.core.use_cases.WishListUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResearchViewModel @Inject constructor(
    private val wishListUseCases: WishListUseCases,
    useCases: FireStoreUseCases
) : ViewModel() {
    val research = useCases.getAllResearchUseCase()

    private val _clickItem = mutableStateOf<ResearchModel?>(null)
    val clickItem: State<ResearchModel?> get() = _clickItem
    private val _isExist = mutableStateOf(false)
    val isExist: State<Boolean> get() = _isExist


    fun onEvent(event: ResearchScreenEvents) {
        when (event) {
            is ResearchScreenEvents.OnItemClick -> {
                _clickItem.value = event.model
                checkExistence(event.model)
            }

            ResearchScreenEvents.ResetClickItem -> {
                _clickItem.value = null
                _isExist.value = false
            }

            is ResearchScreenEvents.OnAddToWishList -> viewModelScope.launch {
                if (event.isAdded) {
                    wishListUseCases.insert(event.model)
                } else {
                    wishListUseCases.deleteById(event.model.key ?: "")
                }
                checkExistence(event.model)
            }
        }
    }

    private fun checkExistence(model: ResearchModel) {
        viewModelScope.launch {
            _isExist.value = wishListUseCases.isResearchExistUseCase(model.key ?: "")
        }
    }
}
