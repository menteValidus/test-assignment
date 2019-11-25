package mente.vali.dailyweather

import org.junit.Test

import org.junit.Assert.*
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*
import java.time.format.DateTimeFormatter.ofLocalizedDate
import java.time.format.FormatStyle


/**
 * Example local currentUnitsLiveData test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    @Test
    fun time_test() {
        var date = ofLocalizedDate(FormatStyle.FULL)
            .withLocale(Locale("ru"))
            .format(LocalDateTime.ofInstant(Instant.ofEpochMilli(1574454314), ZoneId.systemDefault()))
        println(date)
        date = ofLocalizedDate(FormatStyle.LONG)
            .withLocale(Locale("ru"))
            .format(LocalDateTime.ofInstant(Instant.ofEpochMilli(1574454314), ZoneId.systemDefault()))
        println(date)
        date = ofLocalizedDate(FormatStyle.MEDIUM)
            .withLocale(Locale("ru"))
            .format(LocalDateTime.ofInstant(Instant.ofEpochMilli(1574454314), ZoneId.systemDefault()))
        println(date)
        date = ofLocalizedDate(FormatStyle.SHORT)
            .withLocale(Locale("ru"))
            .format(LocalDateTime.ofInstant(Instant.ofEpochMilli(1574454314), ZoneId.systemDefault()))
        println(date)
    }
    @Test
    fun time_pattern_test() {
        val date = Date()
        var pattern = "d M HH:mm"
        var dateFormat = SimpleDateFormat(pattern, Locale("ru"))

        println(dateFormat.format(date))

        pattern = "d MM HH:mm"
        dateFormat = SimpleDateFormat(pattern, Locale("ru"))

        println(dateFormat.format(date))

        pattern = "d MMM HH:mm"
        dateFormat = SimpleDateFormat(pattern, Locale("ru"))

        println(dateFormat.format(date))

        pattern = "d MMMM HH:mm"
        dateFormat = SimpleDateFormat(pattern, Locale("ru"))

        println(dateFormat.format(date))
    }
}
