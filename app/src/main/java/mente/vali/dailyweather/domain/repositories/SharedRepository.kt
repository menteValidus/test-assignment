package mente.vali.dailyweather.domain.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import mente.vali.dailyweather.data.models.ObservableWeather
import mente.vali.dailyweather.data.models.WeatherCondition

/**
 * Репозиторий для хранения данных о текущей погоде.
 */
class SharedRepository constructor(appRepository: Context) {
    /**
     * Стандартный [SharedPreferences].
     */
    private val prefs: SharedPreferences by lazy {
        val ctx = appRepository.applicationContext
        PreferenceManager.getDefaultSharedPreferences(ctx)
    }

    /**
     * Метод для сохранения текущей выбранной единицы измерения.
     */
    fun saveSelectedUnit(units: String) {
        putValue(UNITS to units)
    }

    /**
     * Метод для получения текущей выбранной единицы измерения.
     */
    fun getSelectedUnit() = prefs.getString(UNITS, "metric")!!

    /**
     * Метод для сохранения текущего выбранного города.
     */
    fun saveCity(city: String) {
        putValue(CITY to city)
    }

    /**
     * Метод для получения текущего выбранного города.
     * Стандартное значение - "Таганрог"
     */
    fun getCity() = prefs.getString(CITY, "Таганрог")!!

    /**
     * Метод для сохранения текущих показаний погоды.
     * В случае неинициализированных данных сохраняются стандартные значения.
     */
    fun saveWeatherData(date: String, weather: ObservableWeather) {
        putValue(DATETIME_OF_UPDATE to date)
        putValue(TEMPERATURE to (weather.temperature.value?.toInt() ?: 0))
        putValue(
            WEATHER_CONDITION to (weather.weatherCondition.value?.getConditionName()
                ?: WeatherCondition.NONE.getConditionName())
        )
        putValue(HUMIDITY to (weather.humidity.value?.toInt() ?: 0))
        putValue(PRESSURE to (weather.pressure.value ?: 0))
        putValue(WIND_DIRECTION to (weather.windDirection.value ?: ""))
        putValue(WIND_SPEED to (weather.windSpeed.value ?: 0))
    }
    /**
     * Метод для получения последних сохранённых показаний погоды.
     * Возвращаемое значение Пара <Дата, Показания погоды>.
     * Если полученнные данные - данные прошлого дня, то возвращаемое значение null.
     */
    fun getWeatherDate(): Pair<String, ObservableWeather>? {
        with(prefs) {
            val date = getString(DATETIME_OF_UPDATE, "")!!

            // TODO Check current date with update date. In case they both are not the same day return null.

            val temperature = getInt(TEMPERATURE, 0).toShort()
            val weatherCondition =
                WeatherCondition.getByName(getString(WEATHER_CONDITION, "")!!)
            val humidity = getInt(HUMIDITY, 0).toShort()
            val pressure = getInt(PRESSURE, 0)
            val windDirection = getString(WIND_DIRECTION, "")!!
            val windSpeed = getInt(WIND_SPEED, 0)

            return date to ObservableWeather(temperature, weatherCondition, humidity, pressure, windDirection, windSpeed)
        }
    }

    /**
     * Универсальный метод для сохранения данных в [SharedPreferences].
     */
    private fun putValue(pair: Pair<String, Any>) = with(prefs.edit()) {
        val key = pair.first
        val value = pair.second

        when (value) {
            is String -> putString(key, value)
            is Long -> putLong(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitives types can be stored in Shared Preferences")
        }

        apply()
    }

    companion object {
        // Строковые константы для хранения данных и доступа к ним.
        private const val UNITS = "UNITS"
        private const val CITY = "CITY"
        private const val DATETIME_OF_UPDATE = "DATETIME_OF_UPDATE"
        private const val TEMPERATURE = "TEMPERATURE"
        private const val WEATHER_CONDITION = "WEATHER_CONDITION"
        private const val HUMIDITY = "HUMIDITY"
        private const val PRESSURE = "PRESSURE"
        private const val WIND_DIRECTION = "WIND_DIRECTION"
        private const val WIND_SPEED = "WIND_SPEED"

        /**
         * Объект класса.
         */
        @Volatile
        private var INSTANCE: SharedRepository? = null

        /**
         * Статический метод, возвращающий объект класса.
         */
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: SharedRepository(context).also {
                        INSTANCE = it
                    }
            }
    }
}