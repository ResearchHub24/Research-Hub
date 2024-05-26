package com.atech.core.module

import android.content.Context
import androidx.room.Room
import com.atech.core.room.ResearchHubDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): ResearchHubDatabase =
        Room.databaseBuilder(
            context,
            ResearchHubDatabase::class.java,
            ResearchHubDatabase.DATABASE_NAME
        ).fallbackToDestructiveMigrationFrom()
            .build()

    @Provides
    @Singleton
    fun provideResearchHubDao(
        database: ResearchHubDatabase
    ) = database.researchHubDao()

    @Provides
    @Singleton
    fun provideFacultyDao(
        database: ResearchHubDatabase
    ) = database.facultyDao()

}