package com.atech.student.module

import com.atech.student.utils.StudentNavigationProvider
import com.atech.ui_common.utils.NavigationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object StudentModule {

    @Singleton
    @Provides
    fun provideNavigationProvider() : NavigationProvider =
        StudentNavigationProvider()

}