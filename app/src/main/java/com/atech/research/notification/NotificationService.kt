package com.atech.research.notification

import android.Manifest
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.atech.core.use_cases.AuthUseCases
import com.atech.core.use_cases.SetToken
import com.atech.core.utils.AiyuScope
import com.atech.core.utils.ResearchNotification
import com.atech.core.utils.TAGS
import com.atech.core.utils.convertToInt
import com.atech.research.R
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class NotificationService : FirebaseMessagingService() {
    @Inject
    lateinit var auth: AuthUseCases

    @Inject
    lateinit var setToken: SetToken

    @AiyuScope
    @Inject
    lateinit var scope: CoroutineScope

    override fun onMessageReceived(message: RemoteMessage) {
        super.onMessageReceived(message)
        Log.i(TAGS.INFO.name, "onMessageReceived: Message Received")
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
        val uId = auth.isUserLoggedInUseCase.invoke()
        if (uId != null) {
            scope.launch {
                setToken.invoke(uId) {
                    if (it != null) {
                        Log.e(TAGS.ERROR.name, "onNewToken: $it")
                        return@invoke
                    }
                    Log.i(TAGS.INFO.name, "onNewToken: $token")
                }
            }
        }
    }
}