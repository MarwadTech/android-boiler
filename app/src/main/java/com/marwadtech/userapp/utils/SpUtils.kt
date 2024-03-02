package com.marwadtech.userapp.utils

import android.content.Context
import com.google.gson.Gson
import com.marwadtech.userapp.R
import com.marwadtech.userapp.retrofit.models.response.UserResponseModel

class SpUtils(private val context: Context) {
    val pref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)


    var hasFirstTime: Boolean
        get() = pref.contains(KEY_HAS_FIRST_TIME) && pref.getBoolean(KEY_HAS_FIRST_TIME, true)
        set(hasFirstTime) = storeDataByKey(KEY_HAS_FIRST_TIME, hasFirstTime)

    var isLoggedIn: Boolean
        get() = pref.contains(KEY_IS_LOGGED_IN) && pref.getBoolean(KEY_IS_LOGGED_IN, true)
        set(isLoggedIn) = storeDataByKey(KEY_IS_LOGGED_IN, isLoggedIn)

    var isPrivacyPolicy: Boolean
        get() = pref.contains(KEY_IS_PRIVACY_POLICY) && pref.getBoolean(KEY_IS_PRIVACY_POLICY, false)
        set(isPrivacyPolicy) = storeDataByKey(KEY_IS_PRIVACY_POLICY, isPrivacyPolicy)

    var isAdvanceDialogHide: Boolean
        get() = pref.contains(KEY_IS_ADVANCE_DIALOG) && pref.getBoolean(KEY_IS_ADVANCE_DIALOG, false)
        set(isAdvanceDialogHide) = storeDataByKey(KEY_IS_ADVANCE_DIALOG, isAdvanceDialogHide)

    var interstitial: String
        get() = getDataByKey(KEY_INTERSTITIAL, "0")
        set(interstitial) = storeDataByKey(KEY_INTERSTITIAL, interstitial)

    var accessToken: String
        get() = getDataByKey(ACCESS_TOKEN, "")
        set(accessToken) = storeDataByKey(ACCESS_TOKEN, accessToken)

    var user: UserResponseModel?
        get() {
            val gson = Gson()
            val json = getDataByKey(USER_MODEL, "")
            return gson.fromJson(json, UserResponseModel::class.java)
        }
        set(user) {
            val gson = Gson()
            val json = gson.toJson(user)
            pref.edit().putString(USER_MODEL, json).apply()
        }

    @JvmOverloads
    fun getDataByKey(Key: String, DefaultValue: String = ""): String {
        return if (pref.contains(Key)) {
            pref.getString(Key, DefaultValue).toString()
        } else {
            DefaultValue
        }
    }

    private fun storeDataByKey(key: String, Value: String) {
        pref.edit().putString(key, Value).apply()
    }

    private fun storeDataByKey(key: String, Value: Boolean) {
        pref.edit().putBoolean(key, Value).apply()
    }

    fun logout() {
        pref.edit().remove(KEY_IS_LOGGED_IN).commit()
        pref.edit().remove(USER_MODEL).commit()
        pref.edit().remove(ACCESS_TOKEN).commit()
    }

    companion object {
        private const val KEY_HAS_FIRST_TIME = "EXISTING_USER"
        private const val KEY_IS_LOGIN = "LOGIN"
        private const val KEY_INTERSTITIAL = "INTERSTITIAL"
        private const val KEY_IS_ADVANCE_DIALOG = "ADVANCE_DIALOG"
        private const val KEY_IS_PRIVACY_POLICY = "PRIVACY_POLICY"
        private const val KEY_IS_LOGGED_IN = "key_is_logged_in"
        private const val ACCESS_TOKEN = "accessToken"
        private const val USER_MODEL = "user_model"
    }
}
