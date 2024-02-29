package com.marwadtech.userapp.retrofit.service

import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.NotificationResponseModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ProfileApi {
    @GET("/")
    suspend fun getUserDetails(): Response<BaseModel<UserResponseModel>>

    @POST("/")
    suspend fun updateProfile(
        @Body userRequest: UserRequestModel
    ): Response<BaseModel<UserResponseModel>>
}