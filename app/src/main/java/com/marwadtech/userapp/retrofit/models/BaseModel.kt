package com.marwadtech.userapp.retrofit.models

import com.google.gson.annotations.SerializedName

class BaseModel<T>(
    @SerializedName("success") val success: Boolean = true,
    @SerializedName("code") val statusCode: Int = STATUS_IDLE,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: T? = null,
    @SerializedName("errors") val errors: ArrayList<Error>? = null
) {
    companion object {
        private const val STATUS_IDLE = 0
        private const val STATUS_LOADING = 1
        private const val STATUS_SUCCESS = 2
        private const val STATUS_ERROR = 3

        fun <T> loading(): BaseModel<T> {
            return BaseModel(statusCode = STATUS_LOADING, message = "loading")
        }

        fun <T> loading(data: T?): BaseModel<T> {
            return BaseModel(statusCode = STATUS_LOADING, data = data, message = "loading")
        }

        fun <T> clear(): BaseModel<T> {
            return BaseModel(statusCode = STATUS_IDLE)
        }

        fun <T> success(data: BaseModel<T>): BaseModel<T> {
            return BaseModel(success = true, statusCode = STATUS_SUCCESS, data = data.data, message = data.message)
        }

        fun <T> error(
            data: ErrorResponse?
        ): BaseModel<T> {
            return BaseModel(
                success = false,
                statusCode = STATUS_ERROR,
                message = data?.message,
                errors = data?.errors
            )
        }
    }

    fun isLoading(): Boolean {
        return statusCode == STATUS_LOADING
    }

    fun isSuccessfully(): Boolean {
        return statusCode == STATUS_SUCCESS || statusCode in (200..300)
    }

    fun isError(): Boolean {
        return statusCode == STATUS_ERROR
    }
}
