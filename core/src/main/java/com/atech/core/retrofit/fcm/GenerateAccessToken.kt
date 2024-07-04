package com.atech.core.retrofit.fcm

import android.content.Context
import android.util.Log
import com.atech.core.R
import com.atech.core.utils.TAGS
import com.google.auth.oauth2.GoogleCredentials
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class GenerateAccessToken @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val url = "https://www.googleapis.com/auth/firebase.messaging"
    fun generateToken(): String = try {
        val getDataFromRaw = context.resources.openRawResource(R.raw.firebase_admin)
        val credential = GoogleCredentials.fromStream(getDataFromRaw).createScoped(url)
        credential.refreshIfExpired()
        credential.accessToken.tokenValue
    } catch (e: Exception) {
        Log.e(TAGS.ERROR.name, "generateToken: $e")
        ""
    }
}