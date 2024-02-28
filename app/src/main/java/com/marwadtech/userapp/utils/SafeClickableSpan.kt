package com.marwadtech.userapp.utils

import android.os.SystemClock
import android.text.style.ClickableSpan
import android.view.View

class SafeClickableSpan(private val listener: () -> Unit) : ClickableSpan() {

    private var defaultInterval: Int = 1000
    private var lastClickTime = 0L

    override fun onClick(widget: View) {
        if (SystemClock.elapsedRealtime() - lastClickTime < defaultInterval) {
            return
        }
        lastClickTime = SystemClock.elapsedRealtime()
        listener()
    }
}
