package com.atech.core.model

import androidx.annotation.Keep
import com.atech.core.utils.DateFormat
import com.atech.core.utils.convertLongToTime
import com.atech.core.utils.fromJSON

@Keep
data class TagModel(
    val createdBy: String,
    val name: String,
    val created: Long = System.currentTimeMillis(),
) {
    val formattedTime: String
        get() = created.convertLongToTime(DateFormat.DD_MMM_YYYY.format)

}

@Keep
data class ResearchModel(
    val title: String,
    val description: String,
    val createdBy: String,
    val createdByUID: String,
    val created: Long = System.currentTimeMillis(),
    val deadLine: Long? = null,
    val tags: String = "",
) {
    val formattedTime: String
        get() = created.convertLongToTime(DateFormat.DD_MMM_YYYY.format)

    val formattedDeadline: String
        get() = deadLine?.convertLongToTime(DateFormat.DD_MMM_YYYY.format) ?: "No Deadline"

    val tagsToList: List<TagModel> = fromJSON(tags, List::class.java)
        ?.filterIsInstance<TagModel>()
        ?: emptyList()
}


