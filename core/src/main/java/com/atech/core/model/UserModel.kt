package com.atech.core.model

import androidx.annotation.Keep
import com.atech.core.utils.DateFormat
import com.atech.core.utils.convertLongToTime
import com.google.firebase.firestore.Exclude

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
    val password: String? = null,
    val created: Long = System.currentTimeMillis(),
) {
    @get:Exclude
    val formatedTime: String
        get() = created.convertLongToTime(DateFormat.DD_MMM_YYYY.format)
}
