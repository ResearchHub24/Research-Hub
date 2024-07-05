package com.atech.teacher.ui.send

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.atech.core.retrofit.fcm.Data
import com.atech.core.retrofit.fcm.Message
import com.atech.core.retrofit.fcm.Notification
import com.atech.core.retrofit.fcm.NotificationModel
import com.atech.core.use_cases.FcmUseCases
import com.atech.core.utils.NotificationTopics
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class SendNotificationViewModel @Inject constructor(
    private val useCases: FcmUseCases
) : ViewModel() {
    private val _title = mutableStateOf("")
    val title: State<String> get() = _title


    private var key = ""
    private var created: Long = -1L

    internal fun onEvent(event: SendNotificationEvents) {
        when (event) {


            is SendNotificationEvents.OnTitleChange -> _title.value = event.title
            is SendNotificationEvents.GetDataFromArgs -> {
                key = event.args.key
                created = event.args.created
                _title.value = event.args.title.take(50)
            }

            is SendNotificationEvents.SendNotification -> {
                useCases.sendResearchPublishNotification(
                    model = NotificationModel(
                        message = Message(
                            topic = NotificationTopics.ResearchPublish.topic,
                            notification = Notification(
                                title = "Research",
                                body =
                                title.value
                            ),
                            data = Data(
                                key = key,
                                created = created.toString(),
                            )
                        )
                    ),
                    onSuccess = {
                        event.onSuccess(it?.message)
                    }
                )
            }
        }
    }
}