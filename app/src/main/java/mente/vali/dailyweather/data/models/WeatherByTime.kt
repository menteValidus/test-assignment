package mente.vali.dailyweather.data.models

/**
 * Класс, предназначенный для хранения 3-часовых отчётов по погоде.
 */
data class WeatherByTime(
    val date: Long,
    val temperature: Short,
    val minTemperature: Short,
    val maxTemperature: Short,
    val pressure: Int,
    val humidity: Short, // %
    val weatherCondition: WeatherCondition,
    val cloudiness: Short, // %
    val windSpeed: Int,
    val windDegrees: Short,
    val rainVolume: Int?,
    val snowVolume: Int?
)

/**
 * Состояния погоды.
 */
enum class WeatherCondition {
    CLEAR,
    CLOUDS,
    RAIN,
    THUNDERSTORM,
    SNOW,
    MIST,
    NONE;

    companion object {
        /**
         * Статический метод для конвертации строки в [WeatherCondition].
         * Стандартное значение [WeatherCondition.NONE].
         */
        fun get(conditionString: String): WeatherCondition {
            when (conditionString) {
                "Clear" -> return CLEAR
                "Clouds" -> return CLOUDS
                "Rain" -> return RAIN
                "Thunderstorm" -> return THUNDERSTORM
                "Snow" -> return SNOW
                "Mist" -> return MIST
                else -> return NONE
            }
        }
    }
}