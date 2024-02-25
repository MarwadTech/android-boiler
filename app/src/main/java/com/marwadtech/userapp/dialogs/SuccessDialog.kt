package com.marwadtech.userapp.dialogs

import android.app.Activity
import android.app.Dialog
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.Window
import androidx.appcompat.widget.AppCompatTextView
import com.marwadtech.userapp.R

class SuccessDialog() {

    private lateinit var dialog: Dialog

    constructor(context: Activity,title: String) : this() {
        val dialogView = View.inflate(context, R.layout.dialog_verification_success, null)
        dialog = Dialog(context, R.style.AlertConfirmationDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)
        val titleView = dialogView.findViewById<AppCompatTextView>(R.id.txtVerification)
        title?.apply {
            titleView.text = this
        }
    }

    private fun show() {
        if (!dialog.isShowing)
            dialog.show()
    }

    private fun dismiss() {
        if (dialog.isShowing)
            dialog.dismiss()
    }

    fun setShow(show: Boolean) {
        if (show) {
            show()
            Handler(Looper.getMainLooper()).postDelayed({
                dismiss()
            }, 2500)
        }
        else
            dismiss()
    }
}
