package com.atech.core.retrofit

import androidx.annotation.Keep

@Keep
data class FacultyModel(
    val name: String,
    val email: String,
    val imageUrl: String,
    val profileData: String,
    val areaOfInterest: String,
    val profileUrl: String
)