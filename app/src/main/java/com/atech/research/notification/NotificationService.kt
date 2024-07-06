package com.atech.research.notification

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.atech.core.utils.ResearchNotification
import com.atech.core.utils.convertToInt
import com.atech.research.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import kotlin.random.Random

class NotificationService : FirebaseMessagingService() {
    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.d("AAA", "Called")
        createNotice(message)
    }

    private fun createNotice(message: RemoteMessage) {
        val builder = NotificationCompat.Builder(this, ResearchNotification().notificationChannelId)
            .setSmallIcon(R.mipmap.ic_launcher).setContentTitle(message.notification?.title ?: "")
            .setContentText(message.notification?.body ?: "").setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
        val key = message.data["created"]?.toLong()?.convertToInt() ?: Random.nextInt()
        val managerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this, Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        managerCompat.notify(key, builder.build())
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }
}