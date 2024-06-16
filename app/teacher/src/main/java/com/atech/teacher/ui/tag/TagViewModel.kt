package com.atech.teacher.ui.tag

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.core.model.TagModel
import com.atech.core.use_cases.FirebaseDatabaseUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TagViewModel @Inject constructor(
    private val useCase: FirebaseDatabaseUseCases
) : ViewModel() {
    private val _tagList = mutableStateOf<List<Pair<TagModel, Boolean>>>(emptyList())
    val tags: State<List<Pair<TagModel, Boolean>>> get() = _tagList

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: State<String?> get() = _errorMessage

    init {
        getAllTags()
    }

    private fun getAllTags() {
        useCase.getAllTags
            .invoke(
                onError = {
                    _errorMessage.value = it
                },
                onSuccess = {
                    _tagList.value = it
                }
            )
    }

    fun onEvent(event: TagScreenEvents) {
        when (event) {
            is TagScreenEvents.OnCreateNewTag ->
                useCase.createNewTag(event.tagModel,
                    onError = {
                        _errorMessage.value = it
                    },
                    onSuccess = {
                        _errorMessage.value = null
                        getAllTags()
                        event.onSuccess()
                    }
                )

            is TagScreenEvents.OnDeleteTagClick ->
                useCase.deleteTag.invoke(
                    event.tagModel,
                    onError = {
                        _errorMessage.value = it
                    },
                    onSuccess = {
                        _errorMessage.value = null
                        getAllTags()
                    }
                )
        }
    }

}