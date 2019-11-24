package mente.vali.dailyweather.data.models

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import mente.vali.dailyweather.domain.deserializers.ForecastDeserializer
import mente.vali.dailyweather.util.mphToMps
import java.util.*

/**
 * Класс, хранящий прогноз погоды.
 */
class Forecast(weatherList: List<WeatherByTime>) {
    /**
     * Список всех 3-часовых прогнозов.
     */
    val weatherByTimeList: List<WeatherByTime> = weatherList

    //TODO comments
    val todayForecast: ObservableWeather
        get() {
            return ObservableWeather(weatherByTimeList[getCurrentHourID()])
        }

    companion object {
        // TODO comments
        private fun getCurrentHourID(): Int {
            val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
            return currentHour / 3
        }
        /**
         * Парсинг полученного [JsonObject] с получением объекта [Forecast].
         */
        fun parse(json: String): Forecast {
            val gson = GsonBuilder()
                .registerTypeAdapter(
                    Forecast::class.java,
                    ForecastDeserializer()
                ).create()
            return gson.fromJson(json, Forecast::class.java)
        }
    }
}