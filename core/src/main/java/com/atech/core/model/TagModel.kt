package com.atech.core.model

import androidx.annotation.Keep
import com.google.firebase.database.PropertyName
import com.google.gson.annotations.SerializedName


@Keep
data class TagModel(
    @PropertyName("created_by") @get:PropertyName("created_by")
    @SerializedName("created_by") val createdBy: String = "",
    val name: String = "",
    val created: Long = System.currentTimeMillis(),
)
