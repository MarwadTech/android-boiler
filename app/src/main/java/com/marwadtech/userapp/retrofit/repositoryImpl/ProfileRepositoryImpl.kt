package com.marwadtech.userapp.retrofit.repositoryImpl

import android.app.Application
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import com.marwadtech.userapp.retrofit.repository.ProfileRepository
import com.marwadtech.userapp.retrofit.service.AuthApi
import com.marwadtech.userapp.retrofit.service.ProfileApi
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val app: Application,
    private val profileApi: ProfileApi
) :ProfileRepository{
    override suspend fun getUserDetails(): BaseModel<UserResponseModel> {
        return handleResponse(handleRequest(app){profileApi.getUserDetails()})
    }

    override suspend fun updateProfile(userRequest: UserRequestModel): BaseModel<UserResponseModel> {
        return handleResponse(handleRequest(app){profileApi.updateProfile(userRequest)})
    }

}