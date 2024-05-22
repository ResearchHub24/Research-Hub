package com.atech.teacher.module

import com.atech.teacher.navigation.TeacherNavigationProvider
import com.atech.ui_common.utils.NavigationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object TeacherModule {

    @Singleton
    @Provides
    fun provideNavigationProvider(): NavigationProvider =
        TeacherNavigationProvider()

}