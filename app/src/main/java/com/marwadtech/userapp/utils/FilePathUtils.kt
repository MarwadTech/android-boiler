package com.marwadtech.userapp.utils

import android.content.Context
import android.widget.Toast

class FilePathUtils {

    companion object{
        fun Context.showToast(message: String?){
            Toast.makeText(this,message ?: "",Toast.LENGTH_SHORT).show()
        }
    }
}