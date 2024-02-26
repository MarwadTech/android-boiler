package com.marwadtech.userapp.retrofit.service

import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserAuthResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body userRequestModel: UserRequestModel): Response<BaseModel<UserAuthResponseModel>>

    @POST("auth/check-user")
    suspend fun checkUser(@Body userRequestModel: UserRequestModel): Response<BaseModel<UserAuthResponseModel>>

    @POST("auth/send-otp")
    suspend fun sendOtp(@Body userRequestModel: UserRequestModel): Response<BaseModel<UserAuthResponseModel>>

    @POST("auth/verify-otp")
    suspend fun verifyOtp(@Body userRequestModel: UserRequestModel): Response<BaseModel<UserAuthResponseModel>>

    @POST("auth/create-user")
    suspend fun registerWithOtp(@Body userRequestModel: UserRequestModel): Response<BaseModel<UserAuthResponseModel>>
}
