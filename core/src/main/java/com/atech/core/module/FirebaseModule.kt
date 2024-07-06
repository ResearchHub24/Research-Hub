package com.atech.core.module

import com.atech.core.utils.UserLoggedIn
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.ktx.messaging
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object FirebaseModule {

    @Provides
    @Singleton
    fun provideFirebase(): FirebaseFirestore = Firebase.firestore


    @Provides
    @Singleton
    fun provideRealtimeDatabase(): FirebaseDatabase = Firebase.database


    @Provides
    @Singleton
    fun provideFirebaseAuth(): FirebaseAuth = Firebase.auth

    @Singleton
    @Provides
    fun provideFirebaseRemoteConfig(): FirebaseRemoteConfig = Firebase.remoteConfig


    @Provides
    @Singleton
    @UserLoggedIn
    fun provideUserIsLogIn(
        auth: FirebaseAuth
    ): Boolean = auth.currentUser != null

    @Provides
    @Singleton
    fun provideMessage(): FirebaseMessaging = Firebase.messaging

}