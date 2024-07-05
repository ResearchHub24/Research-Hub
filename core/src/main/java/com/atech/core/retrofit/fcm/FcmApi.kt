package com.atech.core.retrofit.fcm

import retrofit2.http.Body
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
    @Headers(
        "Content-Type: application/json"
    )
    @POST("topics/ResearchPublish")
    suspend fun sendResearchPublishNotification(
        @Body notificationModel: NotificationModel
    )
}
