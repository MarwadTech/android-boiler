package com.marwadtech.userapp.retrofit

import com.google.gson.JsonObject

data class PaginationRequestModel(
    var limit: Int = 10,
    var page: Int = 1,
    var order: JsonObject? = null,
    var search: String? = null,
    var id: String? = null,
    var orderId: String? = null,
    var takenBy: String? = null,
    var isRead: Boolean? = null,
    var value: Int? = null,
    var range: String? = null,
    var title: String? = null,
    var status: String? = null,
    var category: String? = null
)
