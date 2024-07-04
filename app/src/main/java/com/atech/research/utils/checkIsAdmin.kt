package com.atech.research.utils

fun <T> isUserAdmin(invoke: () -> T): T? =
    if (com.atech.core.BuildConfig.IS_ADMIN)
        invoke()
    else null