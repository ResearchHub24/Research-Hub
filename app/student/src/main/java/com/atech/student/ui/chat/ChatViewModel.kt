package com.atech.student.ui.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.atech.core.use_cases.ChatUseCases
import com.atech.core.use_cases.FcmUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val chatUseCase: ChatUseCases,
    private val fcmUseCases: FcmUseCases
) : ViewModel() {
    internal fun getAllMessage(path: String) = chatUseCase.getAllMessage.invoke(path)


    internal fun sendMessage(
        receiverName: String,
        receiverUid: String,
        message: String,
        path: String,
        onComplete: (Exception?) -> Unit = {}
    ) = viewModelScope.launch {
        chatUseCase.sendMessage.invoke(
            receiverName = receiverName,
            receiverUid = receiverUid,
            message = message,
            path = path,
            onComplete = onComplete
        )
    }

    internal fun deleteMessage(
        rootPath: String,
        docPath: String,
        onComplete: (Exception?) -> Unit = {}
    ) = viewModelScope.launch {
        chatUseCase.deleteMessage.invoke(
            rootPath = rootPath,
            docPath = docPath,
            onComplete = onComplete
        )
    }

    internal fun sendPushNotification(
        senderUid: String,
        title: String,
        message: String,
        key: Long,
        onSuccess: (Exception?) -> Unit = {}
    ) = fcmUseCases.sendMessageNotification.invoke(
        senderUid = senderUid,
        title = title,
        message = message,
        key = key,
        onSuccess = onSuccess
    )
}