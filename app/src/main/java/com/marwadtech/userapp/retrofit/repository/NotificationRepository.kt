package com.marwadtech.userapp.retrofit.repository

import com.marwadtech.userapp.retrofit.PaginationModel
import com.marwadtech.userapp.retrofit.PaginationRequestModel
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.IRepository
import com.marwadtech.userapp.retrofit.models.response.NotificationResponseModel

interface NotificationRepository : IRepository {
    suspend fun getAllNotifications(paginationRequestModel: PaginationRequestModel): BaseModel<PaginationModel<List<NotificationResponseModel>>>
}