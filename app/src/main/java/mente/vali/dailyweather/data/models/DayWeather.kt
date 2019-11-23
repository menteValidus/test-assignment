package mente.vali.dailyweather.data.models

/**
 * TODO
 */
class DayWeather(dayWeatherByTimeList: List<WeatherByTime>) {

    val averageTemperature: Short

    init {
        var tempSum = 0
        if (dayWeatherByTimeList.size == DAY_CHUNKS) {
            dayWeatherByTimeList.forEach {
                tempSum += it.temperature
            }
        }
        averageTemperature = (tempSum / DAY_CHUNKS).toShort()
    }

    companion object {
        /**
         * Количество обрабатываемых отчётов по одному дню.
         */
        private const val DAY_CHUNKS = 8
    }
}