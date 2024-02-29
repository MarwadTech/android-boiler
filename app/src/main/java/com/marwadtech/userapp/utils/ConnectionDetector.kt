package com.marwadtech.userapp.utils

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.marwadtech.userapp.R

private fun showNoInternetDialog(context: Context) {
    AlertDialog.Builder(context)
        .setCancelable(false)
        .setTitle(context.getString(R.string.alert))
        .setMessage(context.getString(R.string.error_check_your_internet_connection))
        .setPositiveButton(context.getString(R.string.ok)) { _, _ ->
            val intent = Intent(Settings.ACTION_SETTINGS)
            context.startActivity(intent)
        }.show()
}

fun isConnectedToInternet(context: Context?, showAlert: Boolean): Boolean {
    val connectivity =
        context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork = connectivity.activeNetworkInfo

    return if (activeNetwork != null && activeNetwork.isConnected) {
        true
    } else {
        if (showAlert) {
            showNoInternetDialog(context)
        }
        false
    }
}
