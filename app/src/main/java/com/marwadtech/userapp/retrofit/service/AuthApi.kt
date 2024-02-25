package com.marwadtech.userapp.retrofit.service

import com.marwadtech.userapp.retrofit.models.BaseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/signup")
    suspend fun registerWithOtp(@Body body: Any): Response<BaseModel<Any>>

   }
