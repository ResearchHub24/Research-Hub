package com.atech.core.model

import androidx.annotation.Keep
import com.atech.core.utils.DateFormat
import com.atech.core.utils.convertLongToTime

enum class UserType {
    PROFESSORS,
    STUDENTS,
}

@Keep
data class UserModel(
    val uid: String,
    val email: String,
    val name: String,
    val photoUrl: String? = null,
    val userType: String = UserType.STUDENTS.name,
    private val created: Long = System.currentTimeMillis(),
) {
    val formatedTime: String
        get() = created.convertLongToTime(DateFormat.DD_MMM_YYYY.format)
}
