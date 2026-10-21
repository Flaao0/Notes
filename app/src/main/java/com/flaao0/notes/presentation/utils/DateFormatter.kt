package com.flaao0.notes.presentation.utils

import java.text.DateFormat
import java.text.DateFormat.getDateInstance
import java.util.concurrent.TimeUnit


object DateFormatter {

    fun formatDateToString(timeStamp: Long): String {
        val now = System.currentTimeMillis()
        val diff = now - timeStamp

        val millisInHour = TimeUnit.HOURS.toMillis(1)
        val millisInDay = TimeUnit.DAYS.toMillis(1)
        val formatter = getDateInstance(DateFormat.SHORT)

        return when {
            diff < millisInHour -> "Только что"
            diff < millisInDay -> {
                val hours = TimeUnit.MILLISECONDS.toHours(diff)
                "$hours ч назад"
            }
            else -> {
                formatter.format(timeStamp)
            }
        }
    }
}