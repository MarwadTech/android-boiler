package com.marwadtech.userapp.retrofit.repository

import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.IRepository
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.CommonDataResponseModel
import com.marwadtech.userapp.retrofit.models.response.UserAuthResponseModel

interface AuthRepository : IRepository {

    suspend fun login(userRequestModel: UserRequestModel):BaseModel<UserAuthResponseModel>
    suspend fun loginWithGoogle(userRequestModel: UserRequestModel):BaseModel<UserAuthResponseModel>
    suspend fun checkUser(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel>

    suspend fun sendOtp(userRequestModel: UserRequestModel):BaseModel<UserAuthResponseModel>

    suspend fun verifyOtp(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel>

    suspend fun registerWithOtp(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel>

    suspend fun forgotPassword(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel>

    suspend fun resetPassword(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel>

    suspend fun getCommonData():BaseModel<ArrayList<CommonDataResponseModel>>
}
