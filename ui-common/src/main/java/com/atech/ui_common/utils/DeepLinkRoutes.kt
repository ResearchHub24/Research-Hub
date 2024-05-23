package com.atech.ui_common.utils

import androidx.core.net.toUri
import androidx.navigation.NavController
import androidx.navigation.NavDeepLinkRequest

const val DEEP_LINK_SUFFIX = "android-app://com.atech.research"

enum class DeepLinkRoutes(
    val route: String,
) {
    LOGIN("$DEEP_LINK_SUFFIX/login"),
}
fun NavController.navigateWithDeepLink(route: DeepLinkRoutes) {
    val action = NavDeepLinkRequest.Builder.fromUri(route.route.toUri()).build()
    this.navigate(action)
}