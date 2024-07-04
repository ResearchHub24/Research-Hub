package com.atech.core.use_cases

import com.atech.core.retrofit.fcm.FcmApi
import com.atech.core.retrofit.fcm.GenerateAccessToken
import com.atech.core.retrofit.fcm.NotificationModel
import com.atech.core.utils.AiyuScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FcmUseCases @Inject constructor(
    private val sendResearchPublishNotification: SendResearchPublishNotification
)

data class SendResearchPublishNotification @Inject constructor(
    private val api: FcmApi,
    private val generateAccessToken: GenerateAccessToken,
    @AiyuScope private val scope: CoroutineScope
) {
    operator fun invoke(
        model: NotificationModel, onSuccess: (Exception?) -> Unit = {}
    ) = scope.launch(Dispatchers.IO) {
        try {
            val accessToken = generateAccessToken.generateToken()
            api.sendNotification(
                "Bearer $accessToken", model
            )
            onSuccess.invoke(null)
        } catch (e: Exception) {
            onSuccess.invoke(e)
        }
    }
}