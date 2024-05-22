package com.atech.ui_common.utils

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector

data class NavBarModel(
    @StringRes val title: Int,
    val selectedIcon: ImageVector,
    val unSelectedIcon: ImageVector? = null,
    val route: String = "",
    val destinationName: String = "",
)