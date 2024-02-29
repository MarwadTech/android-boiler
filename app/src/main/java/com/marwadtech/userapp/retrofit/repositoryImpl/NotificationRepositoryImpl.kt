package com.marwadtech.userapp.retrofit.repositoryImpl

import android.app.Application
import com.marwadtech.userapp.retrofit.PaginationModel
import com.marwadtech.userapp.retrofit.PaginationRequestModel
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.response.NotificationResponseModel
import com.marwadtech.userapp.retrofit.repository.NotificationRepository
import com.marwadtech.userapp.retrofit.service.NotificationApi
import javax.inject.Inject

class NotificationRepositoryImpl @Inject constructor(
    private val app: Application,
    private val notificationApi: NotificationApi
) : NotificationRepository {
    override suspend fun getAllNotifications(paginationRequestModel: PaginationRequestModel): BaseModel<PaginationModel<List<NotificationResponseModel>>> {
        return handleResponse(
            handleRequest(app) {
                notificationApi.getAllNotifications(
                    limit = paginationRequestModel.limit,
                    page = paginationRequestModel.page,
                    order = paginationRequestModel.order
                )
            }
        )
    }

}