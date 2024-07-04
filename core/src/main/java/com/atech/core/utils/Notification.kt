package com.atech.core.utils

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi

enum class NotificationTopics(val topic: String) {
    Faculties("FacultiesNotifications"),
    ResearchPublish("ResearchPublish")
}

interface NotificationModel {
    val notificationChannelId: String
    val notificationChannelName: String
    val notificationChannelDescription: String
}

@RequiresApi(Build.VERSION_CODES.O)
fun NotificationModel.createNotificationChannel(context: Context) {
    val noticeChannel = NotificationChannel(
        this.notificationChannelId,
        this.notificationChannelName,
        NotificationManager.IMPORTANCE_HIGH
    )
    noticeChannel.description = this.notificationChannelDescription
    val manager = context.getSystemService(NotificationManager::class.java)
    manager.createNotificationChannel(noticeChannel)
}

data class FacultyNotification(
    override val notificationChannelId: String = "faculty",
    override val notificationChannelName: String = "Faculty",
    override val notificationChannelDescription: String = """
        Notification for Faculty
        Generally send by organization
    """.trimIndent()
) : NotificationModel

data class ResearchNotification(
    override val notificationChannelId: String = "research",
    override val notificationChannelName: String = "Research",
    override val notificationChannelDescription: String = """
        Stay updated with latest research publication
    """.trimIndent()
) : NotificationModel
