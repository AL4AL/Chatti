package com.sajjad.base.presentation

import java.text.SimpleDateFormat
import java.util.*

object DateFormatter {

    @JvmStatic
    fun dateToHourMinute(date: Date, asLocal: Boolean): String {
        return SimpleDateFormat("HH:mm", Locale.US).also {
            if (asLocal) {
                it.timeZone = TimeZone.getDefault()
            } else {
                it.timeZone = TimeZone.getTimeZone("GMT0:00")
            }
        }.format(date)
    }

}