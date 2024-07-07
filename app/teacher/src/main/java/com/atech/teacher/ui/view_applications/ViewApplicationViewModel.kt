package com.atech.teacher.ui.view_applications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.model.ResearchPublishModel
import com.atech.core.use_cases.ChatUseCases
import com.atech.core.use_cases.SelectUseUserCase
import com.atech.core.use_cases.TeacherAuthUserCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ViewApplicationViewModel @Inject constructor(
    private val useCase: TeacherAuthUserCase,
    private val selectUserUserCase: SelectUseUserCase,
    private val chatUseCases: ChatUseCases
) : ViewModel() {
    private val _key = mutableStateOf("")


    private val _submittedForms = mutableStateOf<List<ResearchPublishModel>>(emptyList())
    val submittedForms: State<List<ResearchPublishModel>> get() = _submittedForms

    private var job: Job? = null
    internal fun onEvent(event: ViewApplicationEvents) {
        when (event) {
            is ViewApplicationEvents.SetKeyFromArgs -> {
                _key.value = event.string
                fetchData()
            }

            is ViewApplicationEvents.SelectUserAction -> {
                if (_key.value.isNotBlank())
                    selectUserUserCase.invoke(_key.value,
                        event.ui,
                        action = event.action,
                        onComplete = {
                            event.onComplete.invoke(it?.message)
                            if (it == null)
                                fetchData()
                            viewModelScope.launch {
                                chatUseCases.createChat.invoke(
                                    receiverUid = event.ui,
                                    receiverName = event.name,
                                    message = "Hey you have been selected !!",
                                    receiverProfileUrl = event.profileUrl
                                ) {}
                            }
                        })
            }
        }
    }

    private fun fetchData() {
        job?.cancel()
        job = useCase.getAllSubmittedForm.invoke(_key.value).onEach {
            _submittedForms.value = it
        }.launchIn(viewModelScope)
    }
}