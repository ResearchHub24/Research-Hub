package com.atech.student.ui.resume

import com.atech.core.model.EMPTY_USER
import com.atech.core.model.UserModel

data class ResumeState(
    val userData : UserModel = EMPTY_USER
)