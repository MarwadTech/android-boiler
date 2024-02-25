package com.marwadtech.userapp.dialogs

import android.app.Activity
import android.app.Dialog
import android.view.View
import android.view.Window
import android.widget.Button
import androidx.appcompat.widget.AppCompatImageView
import com.marwadtech.userapp.R

class OtpVerificationDialog() {

    private lateinit var dialog: Dialog

    constructor(context: Activity, listener: () -> Unit) : this() {
        val dialogView = View.inflate(context, R.layout.dialog_otp_verification, null)
        dialog = Dialog(context, R.style.AlertConfirmationDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(dialogView)
        dialog.setCancelable(true)
//        val titleView = dialogView.findViewById<AppCompatTextView>(R.id.)
//        title?.apply {
//            titleView.text = this
//        }

        val imageCLose = dialogView.findViewById<AppCompatImageView>(R.id.imgCancel)
        imageCLose.setOnClickListener() {
            dismiss()
        }
        val btnYes = dialogView.findViewById<Button>(R.id.btnSubmit)
        btnYes.setOnClickListener {
            dismiss()

            listener()
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
        if (show)
            show()
        else
            dismiss()
    }
}
