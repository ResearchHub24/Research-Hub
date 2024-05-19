package com.atech.student.ui.resume

enum class AddEditScreenType {
    DETAILS,
    EDUCATION,
    SKILL
}

/**
 * personalDetails: Triple<name, email, phone>
 */
data class AddScreenState(
    val screenType: AddEditScreenType = AddEditScreenType.DETAILS,
    val personalDetails: Triple<String, String, String> = Triple("", "", "")
)

