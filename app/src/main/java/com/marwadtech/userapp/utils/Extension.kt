package com.marwadtech.userapp.utils

import android.view.View
import android.widget.EditText
import android.widget.TextView

private const val TAG = "Extension"

fun View.setSingleClickListener(onSingleClick: (View) -> Unit) {
    val clickListener = OneClickListener {
        onSingleClick(it)
    }
    setOnClickListener(clickListener)
}

fun View.setSingleClickListener(intervalInMillis: Int, onSingleClick: (View) -> Unit) {
    val clickListener = OneClickListener(intervalInMillis) {
        onSingleClick(it)
    }
    setOnClickListener(clickListener)
}


fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}
fun View.visibleOrGone(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.GONE
}

fun View.visibleOrInvisible(visible: Boolean) {
    visibility = if (visible) View.VISIBLE else View.INVISIBLE
}


fun String?.getFirstName(): String? {
    val words = this?.trim()?.split(" ")

    return when {
        words == null || words.size < 2 -> this
        else -> "${words[0]} ${words[1]}"
    }
}

fun EditText.getValue(): String {
    return this.text.toString().trim()
}

fun TextView.getValue(): String {
    return this.text.toString().trim()
}


fun EditText.isEmpty(): Boolean {
    return this.text.trim().isEmpty()
}

