package com.marwadtech.userapp.retrofit.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LevelRequestModel (
    @SerializedName("level")
    @Expose
    var level: Int? = null,

    @SerializedName("title")
    @Expose
    var title: String? = null,

    @SerializedName("percentage")
    @Expose
    var percentage: Int? = null,

    @SerializedName("image")
    @Expose
    var image: String? = null
): Serializable