/*
 *  Created by aiyu
 *  Copyright (c) 2021 . All rights reserved.
 *  BIT App
 *
 */

package com.atech.research.ui.screens.login.utils

import android.content.Intent
import android.content.IntentSender
import android.util.Log
import com.atech.research.BuildConfig
import com.google.android.gms.auth.api.identity.SignInClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

private const val TAG = "GoogleAuthUiClient"

@SuppressWarnings("DEPRECATION")
@Suppress("DEPRECATION")
class GoogleAuthUiClient(
    private val oneTapClient: SignInClient
) {
    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            if (e is CancellationException) throw e else null
        }
        return result?.pendingIntent?.intentSender
    }


    fun signInWithIntent(data: Intent): Pair<String?, Exception?> = try {
        val credential = oneTapClient.getSignInCredentialFromIntent(data)
        val googleIdToken = credential.googleIdToken
        googleIdToken?.let {
            return it to null
        }
        Pair(null, Exception("No Google ID token"))
    } catch (e: Exception) {
        Pair(null, e)
    }

    suspend fun signOut(
        action: () -> Unit
    ) {
        try {
            oneTapClient.signOut().await()
            action()
        } catch (e: Exception) {
            Log.e(TAG, "signOut: $e")
        }
    }


    private fun buildSignInRequest(): com.google.android.gms.auth.api.identity.BeginSignInRequest {
        val webClient = BuildConfig.firebaseWebClient
        return com.google.android.gms.auth.api.identity.BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions.Builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false).setServerClientId(webClient).build()
            ).setAutoSelectEnabled(true).build()
    }
}