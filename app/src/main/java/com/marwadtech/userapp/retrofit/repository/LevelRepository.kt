package com.marwadtech.userapp.retrofit.repository

import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.IRepository
import com.marwadtech.userapp.retrofit.models.request.LevelRequestModel
import com.marwadtech.userapp.retrofit.models.response.LevelResponseModel

interface LevelRepository : IRepository {
    suspend fun getAllLevels(): BaseModel<ArrayList<LevelResponseModel>>

    suspend fun deleteLevel(levelId: String): BaseModel<LevelResponseModel>

    suspend fun addLevel(levelRequestModel: LevelRequestModel): BaseModel<LevelResponseModel>

    suspend fun updateLevel(
        levelId: String, levelRequestModel: LevelRequestModel
    ): BaseModel<LevelResponseModel>
}