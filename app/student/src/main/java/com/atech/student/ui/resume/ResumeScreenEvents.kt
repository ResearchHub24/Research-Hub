package com.atech.student.ui.resume

import com.atech.core.model.EducationDetails

interface ResumeScreenEvents {
    data object OnPersonalDetailsClick : ResumeScreenEvents
    data class OnPersonalDataEdit(
        val name: String,
        val phone: String
    ) : ResumeScreenEvents

    data class OnPersonalDataSave(
        val onComplete: (String?) -> Unit
    ) : ResumeScreenEvents

    data object UpdateUserDetails : ResumeScreenEvents

    data object OnAddEditEducationClick : ResumeScreenEvents
    data class OnEducationEdit(
        val model: EducationDetails
    ) : ResumeScreenEvents

    data class OnEducationSave(
        val onComplete: (String?) -> Unit
    ) : ResumeScreenEvents

    data object OnAddSkillClick : ResumeScreenEvents

    data class OnSkillClick(val skill: String, val onComplete: (String?) -> Unit) :
        ResumeScreenEvents

    data class FilterResult(val query: String) : ResumeScreenEvents
}