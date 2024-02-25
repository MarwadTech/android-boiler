package com.marwadtech.userapp.retrofit.repositoryImpl

import android.app.Application
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.repository.AuthRepository
import com.marwadtech.userapp.retrofit.service.AuthApi
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val app: Application,
    private val authApi: AuthApi
) : AuthRepository {
    override suspend fun registerWithOtp(body: Any): BaseModel<Any> {
        return handleResponse(handleRequest(app) { authApi.registerWithOtp(body) })
    }

}
