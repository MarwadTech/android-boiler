package com.marwadtech.userapp.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import com.marwadtech.userapp.R

class CustomToast(private val context: Context) : Toast(context) {
    override fun setView(view: View) {
        super.setView(view)
    }

    fun setCustomView(
        message: String,
        description: String? = null,
        toastType: Int
    ) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
//        val binding = ItemCustomToastMassageBinding.inflate(inflater)

        @SuppressLint("InflateParams")
        val view = inflater.inflate(R.layout.item_custom_toast_massage, null)
        val toastText = view.findViewById<TextView>(R.id.txtToastTitle)
        val toastDesc = view.findViewById<TextView>(R.id.txtToastDescription)
        val lytToast = view.findViewById<ConstraintLayout>(R.id.lytToast)
        val toastIcon = view.findViewById<ImageView>(R.id.imgMassageType)
        toastText.text = message
        toastDesc.text = description
        when (toastType) {
            ToastType.isError -> {
                lytToast.setBackgroundResource(R.color.pinkish_red)
                toastIcon.setImageResource(R.drawable.ic_circle_exclamation_mark)
            }
            ToastType.removed -> {
                lytToast.setBackgroundResource(R.color.light_green)
                toastIcon.setImageResource(R.drawable.ic_circle_exclamation_mark)
                toastText.setTextColor(context.getColor(R.color.black))
                toastDesc.setTextColor(context.getColor(R.color.black))
            }
            ToastType.success -> {
                lytToast.setBackgroundResource(R.color.dark_spring_green)
                toastIcon.setImageResource(R.drawable.ic_circle_check)
            }
        }
        setView(view)
        this.show()
    }
}
