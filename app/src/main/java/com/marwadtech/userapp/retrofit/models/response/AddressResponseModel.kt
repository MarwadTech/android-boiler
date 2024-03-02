package com.marwadtech.userapp.retrofit.models.response

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddressResponseModel(
    @SerializedName("id")
    @Expose
    var id: String? = null,

    @SerializedName("user_name")
    @Expose
    var userName: String? = null,

    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null,

    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("user_id")
    @Expose
    var userId: String? = null,

    @SerializedName("is_default")
    @Expose
    var isDefault: Boolean? = null,

    @SerializedName("line_1")
    @Expose
    var lineOne: String? = null,

    @SerializedName("line_2")
    @Expose
    var lineTwo: String? = null,

    @SerializedName("city")
    @Expose
    var city: String? = null,

    @SerializedName("state")
    @Expose
    var state: String? = null,

    @SerializedName("country")
    @Expose
    var country: String? = null,

    @SerializedName("pin_code")
    @Expose
    var pinCode: Int? = null,

    @SerializedName("geo_location")
    @Expose
    var geoLocation: GeoLocation? = null,

    @SerializedName("created_at")
    @Expose
    var createdAt: String? = null,

    @SerializedName("updated_at")
    @Expose
    var updatedAt: String? = null,

    var isSelected: Boolean = false

):Serializable

data class GeoLocation(
    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("coordinates")
    @Expose
    var coordinates: List<Double>? = null
):Serializable
