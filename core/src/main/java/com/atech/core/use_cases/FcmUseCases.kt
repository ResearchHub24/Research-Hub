package com.atech.core.use_cases

import com.atech.core.retrofit.fcm.FcmApi
import com.atech.core.retrofit.fcm.NotificationModel
import com.atech.core.utils.AiyuScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

data class FcmUseCases @Inject constructor(
    val sendResearchPublishNotification: SendResearchPublishNotification
)

data class SendResearchPublishNotification @Inject constructor(
    private val api: FcmApi,
    @AiyuScope private val scope: CoroutineScope
) {
    operator fun invoke(
        model: NotificationModel, onSuccess: (Exception?) -> Unit = {}
    ) = scope.launch(Dispatchers.IO) {
        try {
            api.sendResearchPublishNotification(
                model
            )
            onSuccess.invoke(null)
        } catch (e: Exception) {
            onSuccess.invoke(e)
        }
    }
}