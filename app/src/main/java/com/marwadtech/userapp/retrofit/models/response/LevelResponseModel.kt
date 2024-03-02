package com.marwadtech.userapp.retrofit.models.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LevelResponseModel (
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("level")
    @Expose
    var level: Int? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("percentage")
    @Expose
    var percentage: Int? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null
):Serializable