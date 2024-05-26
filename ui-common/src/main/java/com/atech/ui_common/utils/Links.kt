package com.atech.ui_common.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.openLink(link: String) {
    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
    startActivity(browserIntent)
}