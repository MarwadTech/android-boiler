package com.marwadtech.userapp.ui.screens.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.marwadtech.userapp.retrofit.PaginationModel
import com.marwadtech.userapp.retrofit.PaginationRequestModel
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.AddressRequestModel
import com.marwadtech.userapp.retrofit.models.request.ReviewRequestModel
import com.marwadtech.userapp.retrofit.models.request.UpdatePasswordRequestModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.AddressResponseModel
import com.marwadtech.userapp.retrofit.models.response.ImageResponseModel
import com.marwadtech.userapp.retrofit.models.response.ReviewResponseModel
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

    private val _getUserAddress = MutableLiveData<BaseModel<ArrayList<AddressResponseModel>>>()
    val getUserAddress: LiveData<BaseModel<ArrayList<AddressResponseModel>>>
        get() = _getUserAddress
    fun getUserAddress(){
        viewModelScope.launch {
            _getUserAddress.value =BaseModel.loading()
            _getUserAddress.value = profileRepository.getUserAddress()
            _getUserAddress.value = BaseModel.clear()
        }
    }

    private val _addUserAddress = MutableLiveData<BaseModel<AddressResponseModel>>()
    val addUserAddress: LiveData<BaseModel<AddressResponseModel>>
        get() = _addUserAddress
    fun addUserAddress(addressRequestModel: AddressRequestModel){
        viewModelScope.launch {
            _addUserAddress.value =BaseModel.loading()
            _addUserAddress.value = profileRepository.addUserAddress(addressRequestModel)
            _addUserAddress.value = BaseModel.clear()
        }
    }

    private val _updateUserAddress = MutableLiveData<BaseModel<AddressResponseModel>>()
    val updateUserAddress: LiveData<BaseModel<AddressResponseModel>>
        get() = _updateUserAddress
    fun updateUserAddress(addressId: String,addressRequestModel: AddressRequestModel){
        viewModelScope.launch {
            _updateUserAddress.value =BaseModel.loading()
            _updateUserAddress.value = profileRepository.updateUserAddress(addressId,addressRequestModel)
            _updateUserAddress.value = BaseModel.clear()
        }
    }

    private val _deleteUserAddress = MutableLiveData<BaseModel<AddressResponseModel>>()
    val deleteUserAddress: LiveData<BaseModel<AddressResponseModel>>
        get() = _deleteUserAddress
    fun deleteUserAddress(addressId: String){
        viewModelScope.launch {
            _deleteUserAddress.value =BaseModel.loading()
            _deleteUserAddress.value = profileRepository.deleteUserAddress(addressId)
            _deleteUserAddress.value = BaseModel.clear()
        }
    }

    private val _makeAddressDefault = MutableLiveData<BaseModel<AddressResponseModel>>()
    val makeAddressDefault: LiveData<BaseModel<AddressResponseModel>>
        get() = _makeAddressDefault
    fun makeAddressDefault(addressId: String){
        viewModelScope.launch {
            _makeAddressDefault.value =BaseModel.loading()
            _makeAddressDefault.value = profileRepository.makeAddressDefault(addressId)
            _makeAddressDefault.value = BaseModel.clear()
        }
    }


    private val _deleteUser = MutableLiveData<BaseModel<UserResponseModel>>()
    val deleteUser: LiveData<BaseModel<UserResponseModel>>
        get() = _deleteUser
    fun deleteUser(){
        viewModelScope.launch {
            _deleteUser.value =BaseModel.loading()
            _deleteUser.value = profileRepository.deleteUser()
            _deleteUser.value = BaseModel.clear()
        }
    }

    private val _uploadImage = MutableLiveData<BaseModel<ImageResponseModel>>()
    val uploadImage: LiveData<BaseModel<ImageResponseModel>>
        get() = _uploadImage

    fun uploadImage(doc: Uri, collection: String) {
        viewModelScope.launch {
            _uploadImage.value = BaseModel.loading()
            _uploadImage.value = profileRepository.uploadImage(doc, collection)
            _uploadImage.value = BaseModel.clear()
        }
    }

    private val _deleteImage = MutableLiveData<BaseModel<ImageResponseModel>>()
    val deleteImage: LiveData<BaseModel<ImageResponseModel>>
        get() = _deleteImage

    fun deleteImage(imageId : String) {
        viewModelScope.launch {
            _deleteImage.value = BaseModel.loading()
            _deleteImage.value = profileRepository.deleteImage(imageId)
            _deleteImage.value = BaseModel.clear()
        }
    }

    private val _giveAppRating = MutableLiveData<BaseModel<ReviewResponseModel>>()
    val giveAppRating: LiveData<BaseModel<ReviewResponseModel>>
        get() = _giveAppRating

    fun giveAppRating(reviewRequestModel: ReviewRequestModel) {
        viewModelScope.launch {
            _giveAppRating.value = BaseModel.loading()
            _giveAppRating.value = profileRepository.giveAppRating(reviewRequestModel)
            _giveAppRating.value = BaseModel.clear()
        }
    }

    private val _updatePassword = MutableLiveData<BaseModel<UserResponseModel>>()
    val updatePassword: LiveData<BaseModel<UserResponseModel>>
        get() = _updatePassword

    fun updatePassword(updatePasswordRequestModel: UpdatePasswordRequestModel) {
        viewModelScope.launch {
            _updatePassword.value = BaseModel.loading()
            _updatePassword.value = profileRepository.updatePassword(updatePasswordRequestModel)
            _updatePassword.value = BaseModel.clear()
        }
    }


    private val _getReviews = MutableLiveData<BaseModel<PaginationModel<List<ReviewResponseModel>>>>()
    val getReviews: LiveData<BaseModel<PaginationModel<List<ReviewResponseModel>>>>
        get() = _getReviews

    fun getReviews(paginationRequestModel: PaginationRequestModel) {
        viewModelScope.launch {
            _getReviews.value = BaseModel.loading()
            _getReviews.value = profileRepository.getReviews(paginationRequestModel)
        }
    }

}