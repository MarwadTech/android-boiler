package com.marwadtech.userapp.retrofit.models

data class BaseApiResponse<out T>(
    val status: Int? = null,
    val message: String? = null,
    val error: String? = null,
    val errors: ArrayList<Any>? = null,
    val data: T? = null
)
