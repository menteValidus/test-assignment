package mente.vali.dailyweather.util

import android.os.Build
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*
import kotlin.math.round

private const val barToHgConvertingValue = 1.33322f
// TODO comments
fun windDegreeToWindDirection(degrees: Float): String {
    when (degrees) {
        in 0.0..11.25 -> return "С"
        in 11.25..33.75 -> return "ССВ"
        in 33.75..56.25 -> return "СВ"
        in 56.25..78.75 -> return "ВСВ"
        in 78.75..101.25 -> return "В"
        in 101.25..123.75 -> return "ВЮВ"
        in 123.75..146.25 -> return "ЮВ"
        in 146.25..168.75 -> return "ЮЮВ"
        in 168.75..191.25 -> return "Ю"
        in 191.25..213.75 -> return "ЮЮЗ"
        in 213.75..236.25 -> return "ЮЗ"
        in 236.25..258.75 -> return "ЗЮЗ"
        in 258.75..281.25 -> return "З"
        in 281.25..303.75 -> return "ЗСЗ"
        in 303.75..326.25 -> return "СЗ"
        in 326.25..348.75 -> return "ССЗ"
        in 348.75..360.0 -> return "С"
        else -> return "?"
    }
}

fun barToHg(mbar: Int): Float {
    return round(mbar/barToHgConvertingValue * 100.0f) / 100.0f
}


/**
 * Метод, принимающий время в Long и возвращающий его в формате String.
 */
fun getDateTimeStringFromLong(millis: Long): String {

    // LocalDateTime появилось только в версии Oreo (API 26)
    // И является более точным и предпочтительным.
    // В версиях ниже O используется стандартный Date.
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
         return ZonedDateTime                                   // Represent a moment as seen in the wall-clock time used by the people of a particular region (a time zone).
             .now( ZoneId.of( "Europe/Moscow" ) )             // Capture the current moment as seen in the specified time zone. Returns a `ZonedDateTime` object.
             .format(                                        // Generate text representing the value of this `ZonedDateTime` object.
                 DateTimeFormatter                           // Class controlling the generation of text representing the value of a date-time object.
                     .ofLocalizedDateTime ( FormatStyle.MEDIUM )   // Automatically localize the string representing this date-time value.
                     .withLocale ( Locale("ru") )               // Specify the human language and cultural norms used in localizing.
             )

    } else {
        //TODO return
         return ""
    }

}