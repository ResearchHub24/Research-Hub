package com.atech.student.ui.resume

import android.app.DownloadManager.Query

interface ResumeScreenEvents {
    data object OnPersonalDetailsClick : ResumeScreenEvents
    data class OnPersonalDataEdit(
        val name: String,
        val phone: String
    ) : ResumeScreenEvents

    data object UpdateUserDetails : ResumeScreenEvents

    data object OnAddEditEducationClick : ResumeScreenEvents

    data object OnAddSkillClick : ResumeScreenEvents

    data class OnSkillClick(val skill: String) : ResumeScreenEvents
    data class FilterResult(val query: String) : ResumeScreenEvents
}