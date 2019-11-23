package mente.vali.dailyweather.util

import android.os.Build
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

fun getCurrentDateTime(): String {
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