package com.atech.core.use_cases

import com.atech.core.retrofit.fcm.Data
import com.atech.core.retrofit.fcm.FcmApi
import com.atech.core.retrofit.fcm.Message
import com.atech.core.retrofit.fcm.Notification
import com.atech.core.retrofit.fcm.NotificationModel
import com.atech.core.utils.AiyuScope
import com.atech.core.utils.CollectionName
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.getField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

data class FcmUseCases @Inject constructor(
    val sendResearchPublishNotification: SendResearchPublishNotification,
    val sendMessageNotification: SendMessageNotification
)

data class SendResearchPublishNotification @Inject constructor(
    private val api: FcmApi, @AiyuScope private val scope: CoroutineScope
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

data class SendMessageNotification @Inject constructor(
    private val db: FirebaseFirestore,
    private val api: FcmApi,
    @AiyuScope private val scope: CoroutineScope
) {
    operator fun invoke(
        senderUid: String,
        title: String,
        message: String,
        key: Long,
        onSuccess: (Exception?) -> Unit = {}
    ) = scope.launch(Dispatchers.IO) {
        try {
            val d = db.collection(CollectionName.FCM_TOKEN.value).document(senderUid).get().await()
                .getField<String>("token") ?: ""
            val model = NotificationModel(
                message = Message(
                    to = d,
                    notification = Notification(
                        title = title, body = message
                    ),
                    data = Data(
                        key = key.toString(), created = key.toString()
                    ),
                )
            )
            api.sendMessageNotification(
                model
            )
            onSuccess.invoke(null)
        } catch (e: Exception) {
            onSuccess.invoke(e)
        }
    }
}