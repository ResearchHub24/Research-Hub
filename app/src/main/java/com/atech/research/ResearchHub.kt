package com.atech.research

import android.app.Application
import android.os.Build
import com.atech.core.utils.FacultyNotification
import com.atech.core.utils.NotificationTopics
import com.atech.core.utils.createNotificationChannel
import com.atech.research.utils.isUserAdmin
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ResearchHub : Application() {
    @Inject
    lateinit var fcm: FirebaseMessaging
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            isUserAdmin {
                fcm.subscribeToTopic(NotificationTopics.Faculties.topic)
                FacultyNotification().createNotificationChannel(this)
            }
        }
    }
}