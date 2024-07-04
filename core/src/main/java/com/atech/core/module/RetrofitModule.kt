package com.atech.core.module

import com.atech.core.retrofit.faculty.BASE_URL
import com.atech.core.retrofit.faculty.RetrofitApi
import com.atech.core.retrofit.fcm.FcmApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RetrofitModule {

    @Provides
    @Singleton
    fun provideRetrofitApi(): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideRetrofitService(retrofit: Retrofit): RetrofitApi =
        retrofit.create(RetrofitApi::class.java)


    @Provides
    @Singleton
    fun provideFCMService(): FcmApi =
        Retrofit.Builder().baseUrl(FcmApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(FcmApi::class.java)
}