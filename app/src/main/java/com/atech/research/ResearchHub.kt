package com.atech.research

import android.app.Application
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.work.Configuration
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.atech.core.use_cases.SetTokenWorkManagerUseCase
import com.atech.core.utils.FacultyNotification
import com.atech.core.utils.NotificationTopics
import com.atech.core.utils.ResearchNotification
import com.atech.core.utils.createNotificationChannel
import com.atech.research.utils.runOnFlavors
import com.atech.research.workmanager.UpdateTokenWorker
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ResearchHub : Application(), Configuration.Provider {
    @Inject
    lateinit var fcm: FirebaseMessaging

    @Inject
    lateinit var customWorkerFactory: CustomWorkerFactory

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            runOnFlavors(
                onTeacher = {
                    fcm.subscribeToTopic(NotificationTopics.Faculties.topic)
                    fcm.unsubscribeFromTopic(NotificationTopics.ResearchPublish.topic)
                    FacultyNotification().createNotificationChannel(this)
                },
                onStudent = {
                    fcm.unsubscribeFromTopic(NotificationTopics.Faculties.topic)
                    fcm.subscribeToTopic(NotificationTopics.ResearchPublish.topic)
                    ResearchNotification().createNotificationChannel(this)
                }
            )
        }
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(customWorkerFactory)
            .build()
}

class CustomWorkerFactory @Inject constructor(
    private val useCase: SetTokenWorkManagerUseCase
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? =
        UpdateTokenWorker(
            appContext = appContext,
            workerParams = workerParameters,
            setTokenUseCase = useCase
        )


}