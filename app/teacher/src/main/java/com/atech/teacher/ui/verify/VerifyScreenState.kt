package com.atech.teacher.ui.verify

import com.atech.core.model.TeacherUserModel

data class VerifyScreenState(
    val passwordPair: Pair<String, String> = "" to "",
    val userData: TeacherUserModel? = null,
    val errorMessage: String? = null
)
