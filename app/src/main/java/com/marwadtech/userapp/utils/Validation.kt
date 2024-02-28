package com.marwadtech.userapp.utils

import android.widget.EditText
import java.util.regex.Pattern

fun EditText.mobileNumberValidation(): Boolean {
    val REG = "^(\\+91[\\-\\s]?)?[0]?(91)?[123456789]\\d{9}\$"
    val PATTERN: Pattern = Pattern.compile(REG)
    return this.text.isNotEmpty() && this.text.trim().length == 10 && PATTERN.matcher(this.text).find()
//    return this.text.isNotEmpty() && this.text.trim().length == 10
}

fun EditText.passwordValidation(): Boolean {
    return this.text.isNotEmpty() && this.text.trim().length >= 8
}

fun EditText.emailValidation(): Boolean {
    val emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}".toRegex()
    return this.text.isNotEmpty() && this.text.matches(emailPattern)
}
fun EditText.isOtp(): Boolean {
    return this.text.trim().length == 6
}


fun EditText.otpValidation(): Boolean {
    return this.text.trim().length == 6
}

fun EditText.emptyValidation(): Boolean {
    return this.text.trim().isNotEmpty()
}
fun String.emptyValidation(): Boolean {
    return this.trim().isNotEmpty()
}