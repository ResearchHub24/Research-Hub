package com.atech.core.model

import androidx.annotation.Keep
import com.atech.core.utils.DateFormat
import com.atech.core.utils.convertLongToTime
import com.google.firebase.firestore.PropertyName

import com.google.gson.annotations.SerializedName


@Keep
data class ResearchModel(
    val title: String? = null,
    val description: String? = null,
    @PropertyName("created_by") @get:PropertyName("created_by") val createdBy: String? = null,
    @PropertyName("created_by_uid") @get:PropertyName("created_by_uid") val createdByUID: String? = null,
    val created: Long? = null,
    @PropertyName("dead_line") @get:PropertyName("dead_line") val deadLine: Long? = null,
    val tags: String? = null,
    val key: String? = null,
    val questions: String? = null,
) {
    val formattedTime: String
        get() = created?.convertLongToTime(DateFormat.DD_MMM_YYYY.format) ?: "No Date"

    val formattedDeadline: String
        get() = deadLine?.convertLongToTime(DateFormat.DD_MMM_YYYY.format) ?: "No Deadline"
}

@Keep
data class QuestionModel(
    val question: String? = null,
    val answer: String? = null
)


@Keep
data class ResearchPublishModel(
    val studentName: String? = null,
    val studentEmail: String? = null,
    val studentPhoneNumber: String? = null,
    val answers: String? = null,
    val filledDate: Long = System.currentTimeMillis()
)