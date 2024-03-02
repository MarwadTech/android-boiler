package com.marwadtech.userapp.retrofit.repositoryImpl

import android.app.Application
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.LevelRequestModel
import com.marwadtech.userapp.retrofit.models.response.LevelResponseModel
import com.marwadtech.userapp.retrofit.repository.LevelRepository
import com.marwadtech.userapp.retrofit.service.LevelApi
import javax.inject.Inject

class LevelRepositoryImpl @Inject constructor(
    private val app: Application,
    private val levelApi: LevelApi
) :LevelRepository{
    override suspend fun getAllLevels(): BaseModel<ArrayList<LevelResponseModel>> {
        return handleResponse(handleRequest(app){levelApi.getAllLevels()})
    }

    override suspend fun deleteLevel(levelId: String): BaseModel<LevelResponseModel> {
        return handleResponse(handleRequest(app){levelApi.deleteLevel(levelId)})
    }

    override suspend fun addLevel(levelRequestModel: LevelRequestModel): BaseModel<LevelResponseModel> {
        return handleResponse(handleRequest(app){levelApi.addLevel(levelRequestModel)})
    }

    override suspend fun updateLevel(
        levelId: String,
        levelRequestModel: LevelRequestModel
    ): BaseModel<LevelResponseModel> {
        return handleResponse(handleRequest(app){levelApi.updateLevel(levelId, levelRequestModel)})
    }


}