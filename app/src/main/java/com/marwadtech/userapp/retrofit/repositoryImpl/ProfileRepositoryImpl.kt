package com.marwadtech.userapp.retrofit.repositoryImpl

import android.app.Application
import android.net.Uri
import androidx.core.net.toFile
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
import com.marwadtech.userapp.retrofit.service.ProfileApi
import com.marwadtech.userapp.utils.MultipartUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val app: Application,
    private val profileApi: ProfileApi
) :ProfileRepository{
    override suspend fun getUserDetails(): BaseModel<UserResponseModel> {
        return handleResponse(handleRequest(app){profileApi.getUserDetails()})
    }

    override suspend fun updateProfile(userRequest: UserRequestModel): BaseModel<UserResponseModel> {
        return handleResponse(handleRequest(app){profileApi.updateProfile(userRequest)})
    }

    override suspend fun getUserAddress(): BaseModel<ArrayList<AddressResponseModel>> {
        return handleResponse(handleRequest(app){profileApi.getUserAddress()})
    }

    override suspend fun addUserAddress(addressRequestModel: AddressRequestModel): BaseModel<AddressResponseModel> {
        return handleResponse(handleRequest(app){profileApi.addUserAddress(addressRequestModel)})
    }

    override suspend fun updateUserAddress(
        addressId: String,
        addressRequestModel: AddressRequestModel
    ): BaseModel<AddressResponseModel> {
        return handleResponse(handleRequest(app){profileApi.updateUserAddress(addressId,addressRequestModel)})
    }

    override suspend fun deleteUserAddress(
        addressId: String
    ): BaseModel<AddressResponseModel> {
        return handleResponse(handleRequest(app){profileApi.deleteUserAddress(addressId)})
    }

    override suspend fun makeAddressDefault(addressId: String): BaseModel<AddressResponseModel> {
        return handleResponse(handleRequest(app){profileApi.makeAddressDefault(addressId)})
    }

    override suspend fun uploadImage(uri: Uri, collection: String): BaseModel<ImageResponseModel> {
        val image = MultipartUtils.prepareFilePart(
            app.applicationContext,
            uri,
            filename = "${uri.toFile().name}.jpeg",
            name = "image"
        )
        val collectionRequestBody = RequestBody.create("text/plain".toMediaTypeOrNull(), collection)
        return handleResponse(handleRequest(app) { profileApi.uploadImage(image, collectionRequestBody) })
    }

    override suspend fun deleteImage(imageId: String): BaseModel<ImageResponseModel> {
        return handleResponse(handleRequest(app) { profileApi.deleteImage(imageId)})
    }

    override suspend fun deleteUser(): BaseModel<UserResponseModel> {
        return handleResponse(handleRequest(app){profileApi.deleteUser()})
    }

    override suspend fun giveAppRating(reviewRequestModel: ReviewRequestModel): BaseModel<ReviewResponseModel> {
        return handleResponse(handleRequest(app) { profileApi.giveAppRating(reviewRequestModel)})
    }

    override suspend fun updatePassword(updatePasswordRequestModel: UpdatePasswordRequestModel): BaseModel<UserResponseModel> {
        return handleResponse(handleRequest(app) { profileApi.updatePassword(updatePasswordRequestModel)})
    }

    override suspend fun getReviews(paginationRequestModel: PaginationRequestModel): BaseModel<PaginationModel<List<ReviewResponseModel>>> {
        return handleResponse(
            handleRequest(app) {
                profileApi.getReviews(
                    limit = paginationRequestModel.limit,
                    page = paginationRequestModel.page,
                    order = paginationRequestModel.order
                )
            }
        )
    }
}