package com.marwadtech.userapp.utils

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import android.widget.TimePicker
import java.util.Calendar

class DateTimePicker(private val listener: (String) -> Unit) : DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    var day = 0
    var month: Int = 0
    var year: Int = 0
    var hour: Int = 0
    var minute: Int = 0
    var myDay = 0
    var myMonth: Int = 0
    var myYear: Int = 0
    var myHour: Int = 0
    var myMinute: Int = 0
    var context: Context? = null

    enum class DateMode {
        DISABLE_PAST,
        DISABLE_FUTURE,
        NO_RESTRICTION
    }

    fun dateTimePick(context: Context, dateMode: DateMode) {
        this@DateTimePicker.context = context
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)
        val datePickerDialog =
            DatePickerDialog(context, this, year, month, day)

        when (dateMode) {
            DateMode.DISABLE_PAST -> datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
            DateMode.DISABLE_FUTURE -> datePickerDialog.datePicker.maxDate = System.currentTimeMillis()
            DateMode.NO_RESTRICTION -> {} // No date restrictions
        }
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        myDay = dayOfMonth
        myYear = year
        myMonth = month + 1
        listener("$myYear-$myMonth-$myDay")
    }

    private fun showTimePicker() {
        val calendar: Calendar = Calendar.getInstance()
        hour = calendar.get(Calendar.HOUR)
        minute = calendar.get(Calendar.MINUTE)
        listener("$myYear-$myMonth-$myDay $myHour:$myMinute:00")
        val timePickerDialog = TimePickerDialog(
            context,
            this@DateTimePicker,
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        myHour = hourOfDay
        myMinute = minute
        listener("$myYear-$myMonth-$myDay")
    }
}
