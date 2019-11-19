package mente.vali.dailyweather.utils

import java.util.*

object Utils {
    fun getCurrentHourID(): Int {
        val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return currentHour / 3
    }
}