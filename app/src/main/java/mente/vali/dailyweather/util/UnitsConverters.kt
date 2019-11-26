package mente.vali.dailyweather.util

import kotlin.math.round

private const val barToHgConvertingValue = 1.33322f
private const val mphToMpsConvertingValue = 2.23694f

/**
 * Преобразование направления ветра в градусах в формат [String].
 */
fun windDegreeToWindDirection(degrees: Float): String {
    when (degrees) {
        in 0.0..11.25 -> return "Северное"
        in 11.25..33.75 -> return "СС-Восточное"
        in 33.75..56.25 -> return "С-Восточное"
        in 56.25..78.75 -> return "ВС-Восточное"
        in 78.75..101.25 -> return "Восточное"
        in 101.25..123.75 -> return "ВЮ-Восточное"
        in 123.75..146.25 -> return "Ю-Восточное"
        in 146.25..168.75 -> return "ЮЮ-Восточное"
        in 168.75..191.25 -> return "Южное"
        in 191.25..213.75 -> return "ЮЮ-Западное"
        in 213.75..236.25 -> return "Ю-Западное"
        in 236.25..258.75 -> return "ЗЮ-Западное"
        in 258.75..281.25 -> return "Западное"
        in 281.25..303.75 -> return "ЗС-Западное"
        in 303.75..326.25 -> return "С-Западное"
        in 326.25..348.75 -> return "СС-Западное"
        in 348.75..360.0 -> return "Северное"
        else -> return "?"
    }
}

/**
 * Преобразование мБар в мм. рт. ст.
 */
fun barToHg(mbar: Int): Int {
    return round(mbar.toFloat() / barToHgConvertingValue).toInt()
}

/**
 * Преобразование милей/ч в км/с.
 */
fun mphToMps(mph: Int): Int {
    return round(mph / mphToMpsConvertingValue).toInt()
}