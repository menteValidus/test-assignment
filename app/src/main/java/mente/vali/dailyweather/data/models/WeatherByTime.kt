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
    CLEAR {
        override fun getConditionName() = "Ясно"
    },
    CLOUDS {
        override fun getConditionName() = "Облачно"
    },
    RAIN {
        override fun getConditionName() = "Дождь"
    },
    DRIZZLE {
        override fun getConditionName() = "Мелкий дождь"
    },
    THUNDERSTORM {
        override fun getConditionName() = "Гроза"
    },
    SNOW {
        override fun getConditionName() = "Снег"
    },
    MIST {
        override fun getConditionName() = "Дымка"
    },
    SMOKE {
        override fun getConditionName() = "Дымно"
    },
    HAZE {
        override fun getConditionName() = "Мгла"
    },
    DUST {
        override fun getConditionName() = "Пыль"
    },
    FOG {
        override fun getConditionName() = "Туман"
    },
    SAND {
        override fun getConditionName() = "Песочно"
    },
    ASH {
        override fun getConditionName() = "Пепельно"
    },
    SQUALL {
        override fun getConditionName() = "Шквал"
    },
    TORNADO {
        override fun getConditionName() = "Торнадо"
    },
    NONE {
        override fun getConditionName() = "N/A"
    };

    abstract fun getConditionName(): String

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
                "Drizzle" -> return DRIZZLE
                "Smoke" -> return SMOKE
                "Haze" -> return HAZE
                "Dust" -> return DUST
                "Fog" -> return FOG
                "Sand" -> return SAND
                "Ash" -> return ASH
                "Squall" -> return SQUALL
                "Tornado" -> return TORNADO
                else -> return NONE
            }
        }
    }
}