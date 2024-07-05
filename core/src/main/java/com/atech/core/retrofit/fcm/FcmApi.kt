package com.atech.core.retrofit.fcm

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST



interface FcmApi {
    @Headers(
        "Content-Type: application/json"
    )
    @POST("topics/ResearchPublish")
    suspend fun sendResearchPublishNotification(
        @Body notificationModel: NotificationModel
    )
}
