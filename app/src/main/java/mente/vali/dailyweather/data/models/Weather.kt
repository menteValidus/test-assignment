package mente.vali.dailyweather.data.models

import mente.vali.dailyweather.util.windDegreeToWindDirection

data class Weather(
    val temperature: Short = 0,
    val weatherCondition: WeatherCondition = WeatherCondition.NONE,
    val humidity: Short = 0,
    val pressure: Int = 0,
    val windDirection: String = "",
    val windSpeed: Int = 0
) {


    companion object Factory {
        private const val DAY_CHUNKS = 8

        fun makeTodayWeather(weatherByTime: WeatherByTime) = with(weatherByTime) {
            Weather(
                temperature = temperature,
                weatherCondition = weatherCondition,
                humidity = humidity,
                pressure = pressure,
                windDirection = windDegreeToWindDirection(windDegrees.toFloat()),
                windSpeed = windSpeed
            )
        }

        fun makeDayWeather(weatherByTimeList: List<WeatherByTime>): Weather {
            var tempSum = 0
            var humiditySum = 0
            var pressureSum = 0
            var speedSum = 0

            // Если передан полный прогноз.
            if (weatherByTimeList.size == DAY_CHUNKS) {
                // Получить сумму по всем полям.
                weatherByTimeList.forEach {
                    tempSum += it.temperature
                    humiditySum += it.humidity
                    pressureSum += it.pressure
                    speedSum += it.windSpeed
                }
            }
            // Получить средние значения.
            val avgTemperature = (tempSum / DAY_CHUNKS).toShort()
            val avgHumidity = (humiditySum / DAY_CHUNKS).toShort()
            val avgPressure = pressureSum / DAY_CHUNKS
            val avgSpeed = speedSum / DAY_CHUNKS
            // Если список не пуст, то берём первый прогноз.
            val weatherCondition = if (weatherByTimeList.isNotEmpty()) {
                weatherByTimeList[0].weatherCondition
            } else { // Иначе берём стандартное значение.
                WeatherCondition.NONE
            }

            return Weather(
                temperature = avgTemperature,
                humidity = avgHumidity,
                pressure = avgPressure,
                windSpeed = avgSpeed,
                weatherCondition = weatherCondition
            )
        }
    }
}