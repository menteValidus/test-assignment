package mente.vali.dailyweather.data.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * TODO
 */
class DayWeather(dayWeatherByTimeList: List<WeatherByTime>) {

    val averageTemperature: MutableLiveData<Short>

    init {
        var tempSum = 0
        if (dayWeatherByTimeList.size == DAY_CHUNKS) {
            dayWeatherByTimeList.forEach {
                tempSum += it.temperature
            }
        }
        val avgTemperature = (tempSum / DAY_CHUNKS).toShort()
        averageTemperature = MutableLiveData(avgTemperature)
    }

    companion object {
        /**
         * Количество обрабатываемых отчётов по одному дню.
         */
        private const val DAY_CHUNKS = 8
    }
}