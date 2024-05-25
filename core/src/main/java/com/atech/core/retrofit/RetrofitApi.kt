package com.atech.core.retrofit

import retrofit2.http.GET

const val BASE_URL = "https://www.bitmesra.ac.in/"

interface RetrofitApi {
    @GET("Show_Faculty_List?cid=7&deptid=145")
    suspend fun getPageResponse(): String
}