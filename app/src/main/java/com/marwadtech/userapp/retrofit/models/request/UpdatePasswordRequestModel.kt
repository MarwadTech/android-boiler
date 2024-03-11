package com.marwadtech.userapp.retrofit.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UpdatePasswordRequestModel(
    @SerializedName("current_password")
    @Expose
    var currentPassword: String? = null,

    @SerializedName("new_password")
    @Expose
    var newPassword: String? = null,

    @SerializedName("confirm_new_password")
    @Expose
    var confirmNewPassword: String? = null
) : Serializable