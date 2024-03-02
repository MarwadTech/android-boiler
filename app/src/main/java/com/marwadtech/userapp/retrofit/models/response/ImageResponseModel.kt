package com.marwadtech.userapp.retrofit.models.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class ImageResponseModel(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("sort_order")
    @Expose
    var sortOrder: Int? = 0,

    @SerializedName("disk")
    @Expose
    var disk: String? = null,

    @SerializedName("model_id")
    @Expose
    var modelId: String? = null,

    @SerializedName("collection")
    @Expose
    var collection: String? = null,

    @SerializedName("name")
    @Expose
    var name: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("mime")
    @Expose
    var mime: String? = null,

    @SerializedName("size")
    @Expose
    var size: Int? = 0,

    @SerializedName("path")
    @Expose
    var path: String? = null,
    @SerializedName("pic_thumbnail")
    @Expose
    var picThumbnail: String? = null,
    @SerializedName("pic_medium")
    @Expose
    var picMedium: String? = null,
    @SerializedName("pic_large")
    @Expose
    var picLarge: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null

) : Serializable
