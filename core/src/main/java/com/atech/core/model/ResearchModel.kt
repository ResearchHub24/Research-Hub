package com.atech.core.model

import androidx.annotation.Keep
import com.atech.core.utils.DateFormat
import com.atech.core.utils.convertLongToTime
import com.google.firebase.firestore.PropertyName

import com.google.gson.annotations.SerializedName

@Keep
data class TagModel(
    @SerializedName("created_by") val createdBy: String,
    val name: String,
    val created: Long = System.currentTimeMillis(),
) {
    val formattedTime: String
        get() = created.convertLongToTime(DateFormat.DD_MMM_YYYY.format)

}

@Keep
data class ResearchModel(
    val title: String? = null,
    val description: String? = null,
    @PropertyName("created_by") @get:PropertyName("created_by") val createdBy: String? = null,
    @PropertyName("created_by_uid") @get:PropertyName("created_by_uid") val createdByUID: String? = null,
    val created: Long? = null,
    @PropertyName("dead_line") @get:PropertyName("dead_line") val deadLine: Long? = null,
    val tags: String? = null,
    val key: String? = null
) {
    val formattedTime: String
        get() = created?.convertLongToTime(DateFormat.DD_MMM_YYYY.format) ?: "No Date"

    val formattedDeadline: String
        get() = deadLine?.convertLongToTime(DateFormat.DD_MMM_YYYY.format) ?: "No Deadline"
}


