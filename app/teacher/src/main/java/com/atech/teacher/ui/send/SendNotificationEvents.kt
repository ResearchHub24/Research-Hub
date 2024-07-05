package com.atech.teacher.ui.send

import com.atech.teacher.navigation.SendNotificationScreenArgs

sealed interface SendNotificationEvents {
    data class OnTitleChange(val title: String) : SendNotificationEvents
    data class GetDataFromArgs(val args: SendNotificationScreenArgs) : SendNotificationEvents
    data class SendNotification(
        val onSuccess: (String?) -> Unit
    ) : SendNotificationEvents
}