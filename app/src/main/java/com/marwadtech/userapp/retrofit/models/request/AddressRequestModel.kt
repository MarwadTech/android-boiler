package com.marwadtech.userapp.retrofit.models.request

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.marwadtech.userapp.retrofit.models.response.GeoLocation
import java.io.Serializable

data class AddressRequestModel (
    @SerializedName("type")
    @Expose
    var type: String? = null,

    @SerializedName("user_name")
    @Expose
    var userName: String? = null,

    @SerializedName("phone_number")
    @Expose
    var phoneNumber: String? = null,

    @SerializedName("is_default")
    @Expose
    var isDefault: Boolean? = null,

    @SerializedName("line_1")
    @Expose
    var line1: String? = null,

    @SerializedName("line_2")
    @Expose
    var line2: String? = null,

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
    var pinCode: String? = null,

    @SerializedName("geo_location")
    @Expose
    var geoLocation: GeoLocation? = null
):Serializable