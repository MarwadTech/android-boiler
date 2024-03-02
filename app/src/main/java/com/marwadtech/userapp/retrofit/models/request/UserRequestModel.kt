package com.marwadtech.userapp.retrofit.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserRequestModel(
    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("password")
    @Expose
    var password: String? = null,

    @SerializedName("phone_number_otp")
    @Expose
    var phoneOtp: String? = null,

    @SerializedName("email_otp")
    @Expose
    var emailOtp: String? = null,

    @SerializedName("otp")
    @Expose
    var otp: String? = null,

    @SerializedName("google_id_token")
    @Expose
    var googleIdToken: String? = null,

    @SerializedName("new_password")
    @Expose
    var newPassword: String? = null,

    @SerializedName("confirm_new_password")
    @Expose
    var confirmNewPassword: String? = null,

    @SerializedName("image_id")
    @Expose
    var imageId: String? = null


) : Serializable