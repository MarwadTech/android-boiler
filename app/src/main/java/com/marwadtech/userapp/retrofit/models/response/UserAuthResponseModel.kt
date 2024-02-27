package com.marwadtech.userapp.retrofit.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable
data class UserAuthResponseModel(
    @SerializedName("otp")
    @Expose
    val otp: String? = null,
    @SerializedName("user")
    @Expose
    val user: UserResponseModel? = null,
    @SerializedName("next_route")
    @Expose
    val nextRoute: String? = null,
    @SerializedName("token")
    @Expose
    val token: String? = null
):Serializable
