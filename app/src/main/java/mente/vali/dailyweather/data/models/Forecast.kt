package mente.vali.dailyweather.data.models

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import mente.vali.dailyweather.domain.deserializers.ForecastDeserializer

/**
 * Класс, хранящий прогноз погоды.
 */
class Forecast(weatherList: List<WeatherByTime>) {
    /**
     * Список всех 3-часовых прогнозов.
     */
    val weatherByTimeList: List<WeatherByTime> = weatherList

    companion object {
        /**
         * Парсинг полученного [JsonObject] с получением объекта [Forecast].
         */
        fun parse(json: String) {
            val gson = GsonBuilder()
                .registerTypeAdapter(
                    Forecast::class.java,
                    ForecastDeserializer()
                ).create()
            gson.fromJson(json, Forecast::class.java)
        }
    }
}