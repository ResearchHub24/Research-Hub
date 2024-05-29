package com.atech.student.ui.resume

import com.atech.core.model.EMPTY_USER
import com.atech.core.model.StudentUserModel

data class ResumeState(
    val userData : StudentUserModel = EMPTY_USER
)