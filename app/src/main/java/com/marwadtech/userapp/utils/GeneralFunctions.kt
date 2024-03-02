package com.marwadtech.userapp.utils

import android.content.ActivityNotFoundException
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.Uri
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.core.location.LocationManagerCompat
import com.marwadtech.userapp.ui.auth.AuthActivity
import com.marwadtech.userapp.utils.FilePathUtils.Companion.showToast
import com.onesignal.OneSignal

fun isLocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return LocationManagerCompat.isLocationEnabled(locationManager)
}


fun copyText(context: Context, text: String) {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip = ClipData.newPlainText("Copied", text)
    clipboard.setPrimaryClip(clip)
    context.showToast("Text copied")
}
fun openUrl(context: Context, link: String) {
    val uri = Uri.parse(link)
    val intent = Intent(Intent.ACTION_VIEW, uri)
    context.startActivity(intent)
}

fun openGmail(context: Context, emailAddress: String) {
    val emailIntent = Intent(Intent.ACTION_SENDTO)
    emailIntent.data = Uri.parse("mailto:$emailAddress")

    emailIntent.setPackage("com.google.android.gm")

    try {
        context.startActivity(emailIntent)
    } catch (e: ActivityNotFoundException) {
        // Handle case where Gmail app is not installed
        Toast.makeText(context, "Gmail app not installed", Toast.LENGTH_SHORT).show()
    }
}
fun formatAndSetClickableText(
    textView: TextView,
    maxWords: Int,
    maxLines: Int,
    textString: String?,
    listener: () -> Unit
) {
    val description = textString ?: ""
    if (description.length > maxWords) {
        val trimmedText = description.substring(0, maxWords).lines().take(maxLines)
            .joinToString("\n")
        val moreLink = " ...more"
        val clickableSpan = SafeClickableSpan {
            listener()
        }
        val spannable = SpannableStringBuilder(trimmedText + moreLink)
        spannable.setSpan(
            clickableSpan,
            spannable.length - moreLink.length,
            spannable.length,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        textView.text = spannable
        textView.movementMethod = LinkMovementMethod.getInstance()
    } else {
        textView.text = description
    }
}

fun logout(context: Context,spUtils : SpUtils){
    spUtils.logout()
    OneSignal.logout()
    val intent = Intent(context, AuthActivity::class.java)
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
    context.startActivity(intent)
}
