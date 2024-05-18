package com.atech.core.utils

enum class CollectionName(
    val value: String
) {
    USER("user_database"),
    RESEARCH("research")
}


const val SHARED_PREF_NAME = "ResearchHubPrefs"

enum class PrefKeys(val value: String) {
    IS_LOGIN_SKIP("is_login_skip"),
}