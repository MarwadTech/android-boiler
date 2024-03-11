package com.marwadtech.userapp.retrofit.models.customModels

import java.io.Serializable

data class MapAddressModel(
    var addressLine: String?=null,
    var city: String?=null,
    var state: String?=null,
    var pinCode: String?=null,
    var country: String?=null
):Serializable
