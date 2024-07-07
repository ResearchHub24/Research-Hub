package com.atech.core.retrofit.fcm

import androidx.annotation.Keep

@Keep
data class NotificationModel(
    val message: Message
)

@Keep
data class Message(
    val topic: String? = null,
    val to: String? = null,
    val notification: Notification,
    val data: Data
)

@Keep
data class Notification(
    val title: String, val body: String
)

@Keep
data class Data(
    val key: String,
    val created: String,
    val image: String? = null
)