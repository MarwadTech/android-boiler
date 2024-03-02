package com.marwadtech.userapp.ui.screens.notification

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marwadtech.userapp.retrofit.PaginationModel
import com.marwadtech.userapp.retrofit.PaginationRequestModel
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.response.NotificationResponseModel
import com.marwadtech.userapp.retrofit.repository.NotificationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotificationViewModel @Inject constructor(
    private val notificationRepository: NotificationRepository
) : ViewModel() {

    private val _getAllNotifications = MutableLiveData<BaseModel<PaginationModel<List<NotificationResponseModel>>>>()
    val getAllNotifications: LiveData<BaseModel<PaginationModel<List<NotificationResponseModel>>>>
        get() = _getAllNotifications

    fun getAllNotifications(paginationRequestModel: PaginationRequestModel) {
        viewModelScope.launch {
            _getAllNotifications.value = BaseModel.loading()
            _getAllNotifications.value = notificationRepository.getAllNotifications(paginationRequestModel)
        }
    }

}