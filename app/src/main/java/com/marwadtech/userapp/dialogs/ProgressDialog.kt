package com.marwadtech.userapp.dialogs
//


import android.app.Activity
import android.app.Dialog
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.marwadtech.userapp.R

class ProgressDialog() {

    private lateinit var dialog: Dialog

    constructor(context: Activity) : this() {
        val dialogView = View.inflate(context, R.layout.dialog_progress, null)
        dialog = Dialog(context, R.style.CustomDialog)
        val imgProgressBar = dialogView.findViewById<ImageView>(R.id.imgProgressBar)
        Glide.with(context)
            .asGif()
            .load(R.drawable.loading)
            .into(imgProgressBar)
        dialog.setContentView(dialogView)
        dialog.setCancelable(false)
    }

    fun show() {
        if (!dialog.isShowing)
            dialog.show()
    }

    fun dismiss() {
        if (dialog.isShowing)
            dialog.dismiss()
    }

    fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            show()
        } else {
            dismiss()
        }
    }
}





//import android.app.AlertDialog
//import android.content.Context
//import android.os.Bundle
//import android.view.LayoutInflater
//import com.marwadtech.userapp.MyApp
//import com.marwadtech.userapp.R
//import com.marwadtech.userapp.databinding.DialogProgressBinding
//import javax.inject.Inject
//
//class ProgressDialog (
//    private val context: Context
//) : AlertDialog(context, R.style.ProgressDialog) {
//
//    private lateinit var binding: DialogProgressBinding
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = DialogProgressBinding.inflate(
//            LayoutInflater.from(context),
//            null,
//            false
//        )
//        setContentView(binding.root)
//        setCanceledOnTouchOutside(false)
//        setCancelable(false)
//    }
//}
