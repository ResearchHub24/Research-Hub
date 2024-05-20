package com.atech.core.model

import androidx.annotation.Keep
import com.atech.core.utils.DateFormat
import com.atech.core.utils.convertLongToTime
import com.google.firebase.firestore.Exclude

@Keep
enum class UserType {
    PROFESSORS,
    STUDENTS,
}


@Keep
data class UserModel(
    val uid: String = "",
    val email: String = "",
    val name: String = "",
    val photoUrl: String? = null,
    val userType: String = UserType.STUDENTS.name,
    val password: String? = null,
    val created: Long = System.currentTimeMillis(),
    val phone: String? = null,
    val educationDetails: String? = null,
    val skillList: String? = null
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
    val percentage: String? = null,
)

val DUMMY_EDUCATION_DETAILS = listOf(
    EducationDetails("BCA", "XYZ Institute", "2018", "2022", "8.0"),
    EducationDetails("MCA", "XYZ Institute", "2022", "2024", "8.0"),
    EducationDetails("PhD", "XYZ Institute", "2024", percentage = "8.0"),
)
val EMPTY_USER = UserModel(
    uid = "",
    email = "",
    name = "",
    photoUrl = "",
    userType = "",
    password = "",
    created = 0,
    phone = "",
    educationDetails = "@Keep\n" +
            "data class UserModel(\n" +
            "    val uid: String = \"\",\n" +
            "    val email: String = \"\",\n" +
            "    val name: String = \"\",\n" +
            "    val photoUrl: String? = null,\n" +
            "    val userType: String = UserType.STUDENTS.name,\n" +
            "    val password: String? = null,\n" +
            "    val created: Long = System.currentTimeMillis(),\n" +
            "    val phone: String? = null,\n" +
            "    val educationDetails: String? = null,\n" +
            ") {\n" +
            "    @get:Exclude\n" +
            "    val formatedTime: String\n" +
            "        get() = created.convertLongToTime(DateFormat.DD_MMM_YYYY.format)\n" +
            "}",
)
