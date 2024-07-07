package com.atech.core.model

import androidx.annotation.Keep

@Keep
data class AllChatModel(
    val senderName: String = "",
    val senderUid: String = "",
    val senderProfileUrl: String = "",
    val receiverName: String = "",
    val receiverUid: String = "",
    val receiverProfileUrl: String = "",
    val path : String = "",
    val createdAt: Long = System.currentTimeMillis()
)

@Keep
data class MessageModel(
    val senderName: String = "",
    val senderUid: String = "",
    val receiverName: String = "",
    val receiverUid: String = "",
    val message: String = "",
    val created: Long = System.currentTimeMillis()
)