package com.marwadtech.userapp.utils

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import java.util.concurrent.TimeUnit

object DateUtils {

    private val TAG = DateUtils::class.java.name

    fun String.changeDateFormat(
        newFormat: String = DateFormat.DD_MMM_YYYY
    ): String {
        try {
            val oldFormat = isValidDateFormat(this)
//        Log.e(TAG, "changeDateFormat: $this $oldFormat")

            val currentCalender = Calendar.getInstance()
            val currentDate = currentCalender.time

            val oldDf = SimpleDateFormat(oldFormat)
            oldDf.timeZone = TimeZone.getTimeZone("IST")

            val isToday = isSameDay(oldDf.parse(this), currentCalender.time)
            return when {
                isToday && newFormat == DateFormat.DD_MMM_YYYY_HH_MM_SS -> {
                    val todayFormat = SimpleDateFormat("'Today at' h:mm a")
                    todayFormat.format(oldDf.parse(this))
                }

                isYesterday(
                    oldDf.parse(this),
                    currentCalender
                ) && newFormat == DateFormat.DD_MMM_YYYY_HH_MM_SS -> {
                    val yesterdayFormat = SimpleDateFormat("'Yesterday at' h:mm a")
                    yesterdayFormat.format(oldDf.parse(this)!!)
                }

                else -> {
                    val newDf = SimpleDateFormat(newFormat)
                    return try {
                        oldDf.parse(this)?.let { newDf.format(it) } ?: ""
                    } catch (e: Exception) {
                        ""
                    }
                }
            }
        } catch (e: ParseException) {
            return changeTheDatesFormatCustom(Calendar.getInstance().time.toString())
        }
    }

    private fun isValidDateFormat(date: String): String {
        return try {
            val first = SimpleDateFormat(DateFormat.DATE_Z)
            first.isLenient = false
            first.parse(date)
            DateFormat.DATE_Z
        } catch (e: ParseException) {
            try {
                val second = SimpleDateFormat(DateFormat.YYYY_MM_DD_HH_MM_SS)
                second.isLenient = false
                second.parse(date)
                DateFormat.YYYY_MM_DD_HH_MM_SS
            } catch (e: ParseException) {
                ""
            }
        }
    }

    private fun isSameDay(date1: Date?, date2: Date?): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        val cal2 = Calendar.getInstance()
        cal2.time = date2
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
            cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH) &&
            cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)
    }

    private fun isYesterday(date1: Date?, calendar: Calendar): Boolean {
        val cal1 = Calendar.getInstance()
        cal1.time = date1
        calendar.add(Calendar.DAY_OF_YEAR, -1)
        return isSameDay(cal1.time, calendar.time)
    }

    fun String.calculateWorkExperience(): String {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val startDateObject: Date = dateFormat.parse(this) ?: Date()

        val currentDate = Date()
        val diffInMillis: Long = currentDate.time - startDateObject.time

        val daysDifference = TimeUnit.MILLISECONDS.toDays(diffInMillis)
        val monthsDifference = (daysDifference / 30).toInt()
        val yearsDifference = (daysDifference / 365).toInt()

        val remainingDays = (daysDifference % 365).toInt()
        val remainingMonths = (remainingDays / 30)

        val decimalYears = yearsDifference + (remainingMonths.toDouble() / 12)

        return when {
            decimalYears >= 1 -> String.format("%.1f years", decimalYears)
            monthsDifference > 0 -> "$monthsDifference months"
            else -> "$daysDifference days"
        }
    }

    fun isDateInFuture(dateString: String): Boolean {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDate: Date = inputFormat.parse(dateString) ?: return false

            val currentDate = Date()
            return inputDate.after(currentDate) && inputDate != currentDate
        } catch (e: Exception) {
            return false
        }
    }

    fun changeTheDatesFormatCustom(dateString: String, inputPattern: String = DateFormat.EEE_MMM_DD_HH_MM_SS_ZZZ_YYYY, outputPattern: String = DateFormat.D_MMM_YYYY): String {
        // Parse the date string
        return try {
            val inputFormat = SimpleDateFormat(inputPattern, Locale.ENGLISH)
            val date = inputFormat.parse(dateString)
            // Format the parsed date to your desired output format
            val outputFormat = SimpleDateFormat(outputPattern, Locale.getDefault())

            outputFormat.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            dateString
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isDateInPast(fromDateString: String, toDateString: String): Boolean {
        try {
//            val inputFormat = SimpleDateFormat("d-MM-yyyy", Locale.ENGLISH)
//            val toDateStringInput: Date = inputFormat.parse(toDateString) ?: return false
//            val fromDateStringInput: Date = inputFormat.parse(fromDateString) ?: return false
//            return toDateStringInput.before(fromDateStringInput)

            val dateFormat = DateTimeFormatter.ofPattern("d MMM yyyy")
            val firstDate: LocalDate = LocalDate.parse(fromDateString, dateFormat)
            val secondDate: LocalDate = LocalDate.parse(toDateString, dateFormat)

            return secondDate.isBefore(firstDate)
        } catch (e: Exception) {
            Log.e(TAG, "isDateInPast: $e")
            return false
        }
    }
    fun convertToISO(dateString: String): String {
        try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDate: Date = inputFormat.parse(dateString) ?: return "Invalid"
            val isoFormat = SimpleDateFormat(DateFormat.DATE_Z)
            isoFormat.timeZone = TimeZone.getTimeZone("IST")
            return isoFormat.format(inputDate)
        } catch (e: Exception) {
            return "Error converting date: ${e.message}"
        }
    }
}
