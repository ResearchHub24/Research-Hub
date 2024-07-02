package com.atech.teacher.ui.view_applications

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ViewApplicationViewModel @Inject constructor(

) : ViewModel() {
    private val _key = mutableStateOf("")
    val key: State<String> get() = _key
    fun onEvent(event: ViewApplicationEvents) {
        when (event) {
            is ViewApplicationEvents.SetKeyFromArgs -> {
                _key.value = event.string
//                Todo fetch submitted applications
            }
        }
    }
}