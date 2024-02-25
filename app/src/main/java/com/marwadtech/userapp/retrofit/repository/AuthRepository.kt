package com.marwadtech.userapp.retrofit.repository

import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.IRepository

interface AuthRepository : IRepository {

    suspend fun registerWithOtp(body: Any): BaseModel<Any>
}
