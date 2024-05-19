package com.atech.student.ui.resume

interface ResumeScreenEvents {
    data object OnPersonalDetailsClick : ResumeScreenEvents
    data class OnPersonalDataEdit(
        val name: String,
        val phone: String
    ) : ResumeScreenEvents

    data object UpdateUserDetails : ResumeScreenEvents

    data object OnAddEditEducationClick : ResumeScreenEvents
}