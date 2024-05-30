package com.atech.core.utils

import com.atech.core.BuildConfig

enum class CollectionName(
    val value: String
) {
    USER("user_database"),
    RESEARCH("research"),
    SUBMITTED_FORM("submitted_form"),
}


const val SHARED_PREF_NAME = "ResearchHubPrefs"

enum class PrefKeys(val value: String) {
    IS_LOGIN_SKIP("is_login_skip"),
}

enum class RemoteConfigKeys(val value: String) {
    SKILLS("SKILLS")
}

enum class TAGS {
    ERROR
}

enum class FlavourDimensions(val value: String) {
    Student("student"),
    Teacher("teacher")
}

fun <T> coreCheckIsAdmin(invoke: () -> T): T? =
    if (BuildConfig.IS_ADMIN)
        invoke()
    else null