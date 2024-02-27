package com.marwadtech.userapp.retrofit.models

enum class ErrorCode {
    UNAUTHORIZED,
    NOT_FOUND,
    NO_NETWORK,
    BAD_RESPONSE,
    UNKNOWN
}

data class ErrorResponse(
    val code: ErrorCode = ErrorCode.UNKNOWN,
    val errors: ArrayList<Error>? = null,
    val message: String = ""
)

data class Error(
    val field: String,
    val message: String
)

