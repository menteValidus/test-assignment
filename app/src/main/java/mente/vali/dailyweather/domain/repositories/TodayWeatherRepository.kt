package mente.vali.dailyweather.domain.repositories

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager

/**
 * Репозиторий для хранения данных о текущей погоде.
 */
class TodayWeatherRepository constructor(appRepository: Context) {
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

        /**
         * Объект класса.
         */
        @Volatile
        private var INSTANCE: TodayWeatherRepository? = null

        /**
         * Статический метод, возвращающий объект класса.
         */
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: TodayWeatherRepository(context).also {
                        INSTANCE = it
                    }
            }
    }
}