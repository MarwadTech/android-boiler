package com.marwadtech.userapp.retrofit.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class UserRequestModel (
    @SerializedName("full_name")
    @Expose
    var fullName: String? = null,

    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("password")
    @Expose
    var password: String? = null,

    @SerializedName("phone_otp")
    @Expose
    var phoneOtp: String? = null,

    @SerializedName("email_otp")
    @Expose
    var emailOtp: String? = null,
)