package com.marwadtech.userapp.retrofit.repository

import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.IRepository
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import retrofit2.Response

interface ProfileRepository : IRepository {

    suspend fun getUserDetails(): BaseModel<UserResponseModel>

    suspend fun updateProfile(userRequest: UserRequestModel): BaseModel<UserResponseModel>

    suspend fun getUserAddress(): BaseModel<UserResponseModel>
}