package com.marwadtech.userapp.retrofit.repository

import android.net.Uri
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.IRepository
import com.marwadtech.userapp.retrofit.models.request.AddressRequestModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.AddressResponseModel
import com.marwadtech.userapp.retrofit.models.response.ImageResponseModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel

interface ProfileRepository : IRepository {

    suspend fun getUserDetails(): BaseModel<UserResponseModel>

    suspend fun updateProfile(userRequest: UserRequestModel): BaseModel<UserResponseModel>

    suspend fun getUserAddress(): BaseModel<ArrayList<AddressResponseModel>>

    suspend fun addUserAddress(
        addressRequestModel: AddressRequestModel
    ): BaseModel<AddressResponseModel>
    suspend fun updateUserAddress(
        addressId : String,
        addressRequestModel: AddressRequestModel
    ): BaseModel<AddressResponseModel>
    suspend fun deleteUserAddress(
        addressId : String
    ): BaseModel<AddressResponseModel>
    suspend fun makeAddressDefault(
        addressId : String
    ): BaseModel<AddressResponseModel>

    suspend fun uploadImage(uri: Uri, collection: String): BaseModel<ImageResponseModel>
    suspend fun deleteImage(imageId:String): BaseModel<ImageResponseModel>
    suspend fun deleteUser(): BaseModel<UserResponseModel>
}