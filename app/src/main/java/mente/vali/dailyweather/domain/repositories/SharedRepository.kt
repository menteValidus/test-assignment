package mente.vali.dailyweather.domain.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

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
     */
    fun getCity() = prefs.getString(CITY, "Таганрог")!!

    /**
     * Универсальный метод для сохранения данных в [SharedPreferences].
     */
    private fun putValue(pair: Pair<String, Any>) = with(prefs.edit()) {
        val key = pair.first
        val value = pair.second

        when (value) {
            is String -> putString(key, value)
            is Int -> putInt(key, value)
            is Boolean -> putBoolean(key, value)
            is Long -> putLong(key, value)
            is Float -> putFloat(key, value)
            else -> error("Only primitives types can be stored in Shared Preferences")
        }

        apply()
    }

    companion object {
        // Строковые константы для хранения данных и доступа к ним.
        private const val UNITS = "UNITS"
        private const val CITY = "CITY"

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