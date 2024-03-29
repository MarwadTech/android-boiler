package com.marwadtech.userapp.retrofit.service

import com.google.gson.JsonObject
import com.marwadtech.userapp.retrofit.PaginationModel
import com.marwadtech.userapp.retrofit.models.BaseModel
import com.marwadtech.userapp.retrofit.models.request.AddressRequestModel
import com.marwadtech.userapp.retrofit.models.request.ReviewRequestModel
import com.marwadtech.userapp.retrofit.models.request.UpdatePasswordRequestModel
import com.marwadtech.userapp.retrofit.models.request.UserRequestModel
import com.marwadtech.userapp.retrofit.models.response.AddressResponseModel
import com.marwadtech.userapp.retrofit.models.response.ImageResponseModel
import com.marwadtech.userapp.retrofit.models.response.ReviewResponseModel
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ProfileApi {
    @GET("users/me")
    suspend fun getUserDetails(): Response<BaseModel<UserResponseModel>>

    @PATCH("users/me")
    suspend fun updateProfile(
        @Body userRequest: UserRequestModel
    ): Response<BaseModel<UserResponseModel>>

    @GET("addresses")
    suspend fun getUserAddress(): Response<BaseModel<ArrayList<AddressResponseModel>>>

    @POST("addresses")
    suspend fun addUserAddress(
        @Body addressRequestModel: AddressRequestModel
    ): Response<BaseModel<AddressResponseModel>>

    @PUT("addresses/{id}")
    suspend fun updateUserAddress(
        @Path("id") addressId: String,
        @Body addressRequestModel: AddressRequestModel
    ): Response<BaseModel<AddressResponseModel>>

    @DELETE("addresses/{id}")
    suspend fun deleteUserAddress(
        @Path("id") addressId: String
    ): Response<BaseModel<AddressResponseModel>>

    @PATCH("addresses/{id}/default")
    suspend fun makeAddressDefault(
        @Path("id") addressId: String
    ): Response<BaseModel<AddressResponseModel>>

    @Multipart
    @POST("images")
    suspend fun uploadImage(
        @Part image: MultipartBody.Part,
        @Part("collection") collection: RequestBody
    ): Response<BaseModel<ImageResponseModel>>

    @DELETE("images/{id}")
    suspend fun deleteImage(
        @Path("id") imageId: String
    ):Response<BaseModel<ImageResponseModel>>


    @DELETE("users/me")
    suspend fun deleteUser(): Response<BaseModel<UserResponseModel>>

    @POST("reviews/app")
    suspend fun giveAppRating(
        @Body reviewRequestModel: ReviewRequestModel
    ):Response<BaseModel<ReviewResponseModel>>

    @POST("users/me/update-password")
    suspend fun updatePassword(
        @Body updatePasswordRequestModel: UpdatePasswordRequestModel
    ):Response<BaseModel<UserResponseModel>>

    @GET("reviews")
    suspend fun getReviews(
        @Query("limit") limit: Int = 10,
        @Query("page") page: Int = 1,
        @Query("order") order: JsonObject? = null
    ): Response<BaseModel<PaginationModel<List<ReviewResponseModel>>>>
}