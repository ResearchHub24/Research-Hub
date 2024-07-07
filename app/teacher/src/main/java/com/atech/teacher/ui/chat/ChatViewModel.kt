package com.atech.teacher.ui.chat

import androidx.lifecycle.ViewModel
import com.atech.core.use_cases.ChatUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCases
) : ViewModel() {
    internal fun getAllMessage(path: String) = chatUseCase.getAllMessage.invoke(path)
}