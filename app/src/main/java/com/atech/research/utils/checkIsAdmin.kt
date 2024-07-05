package com.atech.research.utils

fun <T> isUserAdmin(invoke: () -> T): T? =
    if (com.atech.core.BuildConfig.IS_ADMIN)
        invoke()
    else null


fun <T> runOnFlavors(
    onTeacher: () -> T,
    onStudent: () -> T
): T = if (com.atech.core.BuildConfig.IS_ADMIN)
    onTeacher()
else onStudent()