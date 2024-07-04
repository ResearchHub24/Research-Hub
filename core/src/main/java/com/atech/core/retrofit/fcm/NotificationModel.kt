package com.atech.core.retrofit.fcm

import androidx.annotation.Keep


@Keep
enum class Type {
    RESEARCH, FACULTY, SELECTION, ADVERTISEMENT
}

@Keep
data class NotificationModel(
    val message: Message
)

@Keep
data class Message(
    val topic: String, val notification: Notification
)

@Keep
data class Notification(
    val title: String, val body: String
)

@Keep
data class Data(
    val key: String,
    val created: String,
    val type: Type,
    val image: String? = null
)