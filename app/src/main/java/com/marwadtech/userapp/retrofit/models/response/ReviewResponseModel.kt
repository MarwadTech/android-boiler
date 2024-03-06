package com.marwadtech.userapp.retrofit.models.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReviewResponseModel(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("given_by")
    @Expose
    var givenBy: String? = null,

    @SerializedName("taken_by")
    @Expose
    var takenBy: String? = null,

    @SerializedName("comment")
    @Expose
    var comment: String? = null,

    @SerializedName("value")
    @Expose
    var value: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,
):Serializable