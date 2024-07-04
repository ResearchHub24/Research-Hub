package com.atech.core.retrofit.fcm

import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

val DEFAULT_MODEL = NotificationModel(
    message = Message(
        topic = "FacultiesNotifications", notification = Notification(
            title = "Test", body = "Test From Android Client"
        )
    )
)


interface FcmApi {
    companion object {
        const val BASE_URL = "https://fcm.googleapis.com/"
    }

    @Headers(
        "Content-Type: application/json"
    )
    @POST("v1/projects/researchhub-21392/messages:send")
    suspend fun sendNotification(
        @Header("Authorization") api: String, @Body notificationModel: NotificationModel
    )
}
