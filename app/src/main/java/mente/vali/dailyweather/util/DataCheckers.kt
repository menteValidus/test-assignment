package mente.vali.dailyweather.util

import mente.vali.dailyweather.domain.extensions.isSameDay
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

/**
 * Парсинг дата из [String] в [Date].
 */
fun parseDate(dateString: String): Date {
    val parsed: Date
    try {
        val format = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy")
        parsed = format.parse(dateString) ?: Date(0)
    } catch (pe: ParseException) {
        throw IllegalArgumentException(pe)
    }

    return parsed
}

/**
 * Сравнение дат в формате [String].
 * Если они часть одного дня, то возвращаемое значение - true.
 * Иначе - false.
 */
fun isDatesSameDay(dateString1: String, dateString2: String): Boolean {
    return parseDate(dateString1).isSameDay(parseDate(dateString2))
}