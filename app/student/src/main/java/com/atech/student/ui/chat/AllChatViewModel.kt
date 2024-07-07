package com.atech.student.ui.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import com.atech.core.use_cases.ChatUseCases
import com.atech.core.utils.TAGS
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AllChatViewModel @Inject constructor(
    chatUseCase: ChatUseCases
) : ViewModel() {
    val allChats = chatUseCase.getAllChats.invoke(forAdmin = false) {
        Log.e(TAGS.ERROR.name, "Error Loading Messages $it")
    }
}