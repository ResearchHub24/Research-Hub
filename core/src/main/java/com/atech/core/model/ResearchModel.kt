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
    val createdBy: String,
    val title: String,
    val description: String,
    val created: Long = System.currentTimeMillis(),
    val tags: String,
) {
    val formattedTime: String
        get() = created.convertLongToTime(DateFormat.DD_MMM_YYYY.format)

    val tagsToList: List<TagModel> = fromJSON(tags, List::class.java)
        ?.filterIsInstance<TagModel>()
        ?: emptyList()
}


