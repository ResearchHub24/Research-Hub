package com.atech.ui_common.common.chat

import com.atech.core.model.MessageModel

sealed interface ChatsEvent {
    data class SendMessage(val message: MessageModel) : ChatsEvent
}