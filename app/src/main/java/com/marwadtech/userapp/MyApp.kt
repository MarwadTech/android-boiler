package com.marwadtech.userapp

import android.app.Application
import com.onesignal.OneSignal
import com.onesignal.debug.LogLevel
import dagger.hilt.android.HiltAndroidApp

const val ONESIGNAL_APP_ID = "eeafc354-2658-4846-a2ab-9b3f9e894060"

@HiltAndroidApp
class MyApp : Application(){

    override fun onCreate() {
        super.onCreate()
        // Verbose Logging set to help debug issues, remove before releasing your app.
        OneSignal.Debug.logLevel = LogLevel.VERBOSE

        // OneSignal Initialization
        OneSignal.initWithContext(this, ONESIGNAL_APP_ID)
    }
}
