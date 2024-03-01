package com.marwadtech.userapp.ui.screens.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import com.marwadtech.userapp.retrofit.repository.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository
) : ViewModel() {

    private val _getUserDetails = MutableLiveData<BaseModel<UserResponseModel>>()
    val getUserDetails: LiveData<BaseModel<UserResponseModel>>
        get() = _getUserDetails
    fun getUserDetails() {
        viewModelScope.launch {
            _getUserDetails.value = BaseModel.loading()
            _getUserDetails.value = profileRepository.getUserDetails()
            _getUserDetails.value = BaseModel.clear()
        }
    }

    private val _updateProfile = MutableLiveData<BaseModel<UserResponseModel>>()
    val updateProfile: LiveData<BaseModel<UserResponseModel>>
        get() = _updateProfile
    fun updateProfile(userRequestModel: UserRequestModel) {
        viewModelScope.launch {
            _updateProfile.value = BaseModel.loading()
            _updateProfile.value = profileRepository.updateProfile(userRequestModel)
            _updateProfile.value = BaseModel.clear()
        }
    }

    private val _getUserAddress = MutableLiveData<BaseModel<UserResponseModel>>()

    val getUserAddress: LiveData<BaseModel<UserResponseModel>>
        get() = _getUserAddress
    fun getUserAddress(){
        viewModelScope.launch {
            _getUserAddress.value =BaseModel.loading()
            _getUserAddress.value = profileRepository.getUserAddress()
            _getUserAddress.value = BaseModel.clear()
        }
    }

}