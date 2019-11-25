package mente.vali.dailyweather.data.extensions

import java.text.SimpleDateFormat
import java.util.*

private const val SECOND = 1000L
private const val MINUTE = 60 * SECOND
private const val HOUR = 60 * MINUTE
private const val DAY = 24 * HOUR

fun Date.isSameDay(date: Date): Boolean {
    val day1 = this.time / DAY
    val day2 = date.time / DAY
    return day1 == day2
}

fun Date.presentationDateTimeFormat(): String {
    val pattern = "d MMMM HH:mm"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

private fun Date.presentationDateFormat(): String {
    val pattern = "d MMMM"
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))
    return dateFormat.format(this)
}

fun Date.presentationDateWithDayOffsetFormat(dayOffset: Int = 0): String {
    return Date(Date().time + DAY * dayOffset).presentationDateFormat()
}