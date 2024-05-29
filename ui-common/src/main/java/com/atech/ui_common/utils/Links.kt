package com.atech.ui_common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.atech.ui_common.common.toast

fun Context.openLink(link: String) {
    try {
        val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        startActivity(browserIntent)
    } catch (e: Exception) {
        toast(context = this, message = e.message ?: "Error opening link")
    }
}