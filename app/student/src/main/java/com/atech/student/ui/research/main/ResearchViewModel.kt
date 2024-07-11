package com.atech.student.ui.research.main

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.model.ResearchModel
import com.atech.core.model.TagModel
import com.atech.core.use_cases.FireStoreUseCases
import com.atech.core.use_cases.FirebaseDatabaseUseCases
import com.atech.core.use_cases.StudentPrefUseCases
import com.atech.core.use_cases.WishListUseCases
import com.atech.core.utils.DataState
import com.atech.core.utils.UserLoggedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResearchViewModel @Inject constructor(
    private val wishListUseCases: WishListUseCases,
    private val useCases: FireStoreUseCases,
    private val realtimeDb: FirebaseDatabaseUseCases,
    private val dataStore: StudentPrefUseCases,
    @UserLoggedIn val isUserLogIn: Boolean
) : ViewModel() {


    private var job: Job? = null
    private val _researchWithDataState =
        mutableStateOf<DataState<List<ResearchModel>>>(DataState.Loading)
    val researchWithDataState: State<DataState<List<ResearchModel>>> get() = _researchWithDataState

    private val _allTags = mutableStateOf<List<TagModel>>(emptyList())
    val allTags: State<List<TagModel>> get() = _allTags


    private val _clickItem = mutableStateOf<ResearchModel?>(null)
    val clickItem: State<ResearchModel?> get() = _clickItem
    private val _isExist = mutableStateOf(false)
    val isExist: State<Boolean> get() = _isExist
    private val _isFromArgs = mutableStateOf(false)
    val isFromArgs: State<Boolean> get() = _isFromArgs


    private fun loadData() {
        realtimeDb.getAllTags.invoke(onError = {
            _allTags.value = emptyList()
        }) { tags ->
            _allTags.value = tags.map { it.first }
        }
        loadResearch()
    }

    init {
        loadData()
    }

    private fun loadResearch() {
        job?.cancel()
        job = useCases.getAllResearchUseCase.invoke { isEmpty ->
            if (isEmpty) viewModelScope.launch {
                wishListUseCases.deleteAll.invoke()
            }
        }.onEach {
            _researchWithDataState.value = com.atech.core.utils.DataState.Success(it)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: ResearchScreenEvents) {
        when (event) {
            is ResearchScreenEvents.OnItemClick -> {
                _isFromArgs.value = false
                _clickItem.value = event.model
                checkExistence(event.model)
            }

            ResearchScreenEvents.ResetClickItem -> {
                _clickItem.value = null
                _isExist.value = false
                _isFromArgs.value = false
            }

            is ResearchScreenEvents.OnAddToWishList -> viewModelScope.launch {
                if (event.isAdded) {
                    wishListUseCases.insert(event.model)
                } else {
                    wishListUseCases.deleteById(event.model.key ?: "")
                }
                checkExistence(event.model)
            }

            is ResearchScreenEvents.SetDataFromArgs -> {
                viewModelScope.launch {
                    val model = wishListUseCases.getById(event.key)
                    _clickItem.value = model
                    checkExistence(model ?: return@launch)
                    _isFromArgs.value = true
                }
            }

            is ResearchScreenEvents.DeleteResearchNotInKeys -> viewModelScope.launch {
                if (event.list.isEmpty()) {
                    return@launch
                }
                wishListUseCases.deleteResearchNotInKeysUseCase(wishListUseCases.getAllList.invoke()
                    .filter { model ->
                        event.list.contains(model.key)
                    }.map { it.key ?: "" })
            }

        }
    }

    private fun checkExistence(model: ResearchModel) {
        viewModelScope.launch {
            _isExist.value = wishListUseCases.isResearchExistUseCase(model.key ?: "")
        }
    }
}
