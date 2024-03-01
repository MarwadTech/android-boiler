package com.marwadtech.userapp.retrofit.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserResponseModel(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null,

    @SerializedName("email")
    @Expose
    var email: String? = null,

    @SerializedName("status")
    @Expose
    var status: String? = null,

    @SerializedName("role")
    @Expose
    var role: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("addresses")
    @Expose
    var addresses: ArrayList<UserAddressResponseModel>? = null
):Serializable