package com.marwadtech.userapp.retrofit.models.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CommonDataResponseModel(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("key")
    @Expose
    var key: String? = null,

    @SerializedName("data")
    @Expose
    var data: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
) : Serializable