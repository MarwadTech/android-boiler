package com.marwadtech.userapp.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.annotation.NonNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.net.URLConnection

object MultipartUtils {

    @NonNull
    fun prepareFilePart(
        mContext: Context,
        fileUri: Uri,
        name: String = "avatar",
        filename: String
    ): MultipartBody.Part {
        var file: File = File(FilePathUtil.getPath(mContext, fileUri) ?: fileUri.toString())
        var mediatype = URLConnection.guessContentTypeFromName(file.name)?.toMediaTypeOrNull()
        Log.e(TAG, "prepareFilePart: $mediatype")
//        var mediatype: MediaType? = mContext.contentResolver.getType(fileUri)?.toMediaTypeOrNull()
        if (mediatype == null) {
//            Log.e(TAG, "prepareFilePart: media type is null")
            mediatype = "image/jpeg".toMediaTypeOrNull()
        }
        Log.e(TAG, "prepareFilePart: $mediatype")
        val requestFile = RequestBody.create(
            mediatype,
            file
        )
        return MultipartBody.Part.createFormData(name, filename, requestFile)
    }

    private val TAG = MultipartUtils::class.java.name
}
