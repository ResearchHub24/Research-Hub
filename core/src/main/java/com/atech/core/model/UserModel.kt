package com.atech.core.model

import androidx.annotation.Keep
import com.atech.core.utils.DateFormat
import com.atech.core.utils.convertLongToTime
import com.atech.core.utils.toJSON
import com.google.firebase.firestore.Exclude

@Keep
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
    val phone: String? = null,
    val educationDetails: String? = null,
) {
    @get:Exclude
    val formatedTime: String
        get() = created.convertLongToTime(DateFormat.DD_MMM_YYYY.format)
}

data class EducationDetails(
    val degree: String,
    val institute: String,
    val startYear: String,
    val endYear: String? = null,
    val percentage: Double? = null,
)

val DUMMY_EDUCATION_DETAILS = listOf(
    EducationDetails("BCA", "XYZ Institute", "2018", "2022", 8.0),
    EducationDetails("MCA", "XYZ Institute", "2022", "2024", 8.0),
    EducationDetails("PhD", "XYZ Institute", "2024", percentage = 8.0),
)
val EMPTY_USER = UserModel(
    uid = "",
    email = "xyz@gmail.com",
    name = "XYZ",
    photoUrl = "",
    userType = "",
    password = "",
    created = 0,
    phone = "+91 1234567890",
    educationDetails = toJSON(DUMMY_EDUCATION_DETAILS),
)
