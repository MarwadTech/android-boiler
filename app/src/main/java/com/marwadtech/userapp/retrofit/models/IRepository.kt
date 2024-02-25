package com.marwadtech.userapp.retrofit.models

import android.app.Application
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.MalformedJsonException
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.net.ConnectException

/**
 * Base interface to be implemented by all repositories
 */
interface IRepository {

    companion object {
        private val TAG = IRepository::class.java.name
    }

    suspend fun <T> handleRequest(app: Application, call: suspend () -> Response<T>): ApiResponse<T> {
        return try {
            val apiResponse = call.invoke()
            if (apiResponse.isSuccessful) {
                ApiResponse.Success(apiResponse.body())
            } else {
                handleFailureResponse(apiResponse.errorBody(), app)
            }
        } catch (ex: Exception) {
            Log.e(TAG, ex.localizedMessage)
            ex.printStackTrace()
            handleException(ex)
        }
    }

    fun <T> handleFailureResponse(response: ResponseBody?, app: Application): ApiResponse<T> {
        response?.let {
            val jsonObject = JSONObject(it.string())
            try {
                val gson = Gson()
                val code = if (jsonObject.has("statusCode")) {
                    jsonObject.getInt("statusCode")
                } else {
                    0
                }
                val errorMessage = if (jsonObject.has("message")) {
                    jsonObject.getString("message")
                } else {
                    ""
                }
                val error = if (jsonObject.has("error")) {
                    jsonObject.getString("error")
                } else {
                    ""
                }
                val errors = if (jsonObject.has("errors")) {
                    val typeToken = object : TypeToken<ArrayList<Error>>() {}.type
                    Gson().fromJson<ArrayList<Error>>(jsonObject.getString("errors"), typeToken)
                } else {
                    null
                }
                val errorCode: ErrorCode = when (code) {
                    401 -> ErrorCode.UNAUTHORIZED
                    404 -> ErrorCode.NOT_FOUND
                    403 -> ErrorCode.BAD_RESPONSE
                    else -> ErrorCode.UNKNOWN
                }

                return ApiResponse.Failure(ErrorResponse(errorCode, errors, errorMessage))
            } catch (ex: Exception) {
                Log.d(
                    "BaseRepository",
                    ex.message ?: "Unknown error while handling failure response"
                )
            }
        }
        return ApiResponse.Failure(ErrorResponse(ErrorCode.UNKNOWN))
    }

    private fun <T> handleException(exception: Exception?): ApiResponse<T> {
        exception?.let {
            val errorResponse = if (exception is ConnectException) {
                ErrorResponse(ErrorCode.NO_NETWORK)
            } else if (exception is MalformedJsonException) {
                ErrorResponse(ErrorCode.BAD_RESPONSE)
            } else {
                ErrorResponse(ErrorCode.UNKNOWN)
            }
            return ApiResponse.Failure(errorResponse)
        }
        return ApiResponse.Failure(ErrorResponse(ErrorCode.UNKNOWN))
    }

    fun <T> handleResponse(result: ApiResponse<BaseModel<T>>): BaseModel<T> {
        return result.data?.apply {
            if (result.isSuccessful) BaseModel.success(this) else BaseModel.error(result.errorResponse)
        } ?: run {
            BaseModel.error(result.errorResponse)
        }
    }
}
