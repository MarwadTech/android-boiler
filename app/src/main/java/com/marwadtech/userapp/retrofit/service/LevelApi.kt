package com.marwadtech.userapp.retrofit.service

import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.LevelRequestModel
import com.marwadtech.userapp.retrofit.models.response.LevelResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface LevelApi {
    @GET("levels")
    suspend fun getAllLevels(): Response<BaseModel<ArrayList<LevelResponseModel>>>

    @DELETE("levels/{id}")
    suspend fun deleteLevel(
        @Path("id") levelId: String
    ): Response<BaseModel<LevelResponseModel>>

    @POST("levels")
    suspend fun addLevel(
        @Body levelRequestModel: LevelRequestModel
    ):Response<BaseModel<LevelResponseModel>>

    @PATCH("levels/{id}")
    suspend fun updateLevel(
        @Path("id") levelId: String,
        @Body levelRequestModel: LevelRequestModel
    ):Response<BaseModel<LevelResponseModel>>
}