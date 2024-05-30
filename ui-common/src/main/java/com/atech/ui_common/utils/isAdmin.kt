package com.atech.ui_common.utils

import androidx.compose.runtime.Composable
import com.atech.ui_common.BuildConfig

@Composable
fun IsAdmin(invoke: @Composable () -> Unit) =
    if (BuildConfig.IS_ADMIN) invoke() else null


@Composable
fun IsStudent(invoke: @Composable () -> Unit) =
    if (BuildConfig.IS_ADMIN.not()) invoke() else null

