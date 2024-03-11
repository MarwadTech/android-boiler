package com.marwadtech.userapp.retrofit.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ReviewRequestModel(
    @SerializedName("value")
    @Expose
    var value: Double? = null,

    @SerializedName("comment")
    @Expose
    var comment: String? = null,
):Serializable