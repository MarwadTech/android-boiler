package com.marwadtech.userapp.retrofit.service

import com.google.gson.JsonObject
import com.marwadtech.userapp.retrofit.PaginationModel
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.NotificationResponseModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NotificationApi {
    @GET("/")
    suspend fun getAllNotifications(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("order") order: JsonObject? = null
    ): Response<BaseModel<PaginationModel<List<NotificationResponseModel>>>>
}