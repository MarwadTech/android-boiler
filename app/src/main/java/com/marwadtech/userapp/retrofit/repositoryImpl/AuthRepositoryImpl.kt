package com.marwadtech.userapp.retrofit.repositoryImpl

import android.app.Application
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.CommonDataResponseModel
import com.marwadtech.userapp.retrofit.models.response.UserAuthResponseModel
import com.marwadtech.userapp.retrofit.repository.AuthRepository
import com.marwadtech.userapp.retrofit.service.AuthApi
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val app: Application,
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun login(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel> {
        return handleResponse(handleRequest(app){authApi.login(userRequestModel)})
    }

    override suspend fun loginWithGoogle(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel> {
        return handleResponse(handleRequest(app){authApi.loginWithGoogle(userRequestModel)})
    }

    override suspend fun checkUser(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel> {
       return handleResponse(handleRequest(app){authApi.checkUser(userRequestModel)})
    }

    override suspend fun sendOtp(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel> {
        return handleResponse(handleRequest(app){authApi.sendOtp(userRequestModel)})
    }

    override suspend fun verifyOtp(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel> {
        return handleResponse(handleRequest(app){authApi.verifyOtp(userRequestModel)})
    }
    override suspend fun registerWithOtp(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel> {
        return handleResponse(handleRequest(app){authApi.registerWithOtp(userRequestModel)})
    }

    override suspend fun forgotPassword(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel> {
        return handleResponse(handleRequest(app){authApi.forgotPassword(userRequestModel)})
    }

    override suspend fun resetPassword(userRequestModel: UserRequestModel): BaseModel<UserAuthResponseModel> {
        return handleResponse(handleRequest(app){authApi.resetPassword(userRequestModel)})
    }

    override suspend fun getCommonData(): BaseModel<ArrayList<CommonDataResponseModel>> {
        return handleResponse(handleRequest(app){authApi.getCommonData()})
    }

}
