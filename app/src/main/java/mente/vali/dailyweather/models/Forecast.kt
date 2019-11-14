package mente.vali.dailyweather.models

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import mente.vali.dailyweather.deserializers.ForecastDeserializer
import org.json.JSONArray

/**
 * Класс, хранящий прогноз погоды.
 */
class Forecast(weatherList: ArrayList<WeatherByTime>, sunriseTime: String, sunsetTime: String) {
    /**
     * Список всех 3-часовых прогнозов.
     */
    val weatherByTimeList: ArrayList<WeatherByTime>
    /**
     * Время рассвета в выбранном городе.
     */
    val sunriseTime: String
    /**
     * Время заката в выбранном городе.
     */
    val sunsetTime: String

    /**
     * Стандартный конструктор.
     */
    init {
        weatherByTimeList = weatherList
        this.sunriseTime = sunriseTime
        this.sunsetTime = sunsetTime
    }

    companion object {
        /**
         * Парсинг полученного [JsonObject] с получением объекта [Forecast].
         */
        fun parse(json: String) {
            // TODO parse JSON
            val gson = GsonBuilder()
                .registerTypeAdapter(Forecast::class.java, ForecastDeserializer()).create()
            gson.fromJson(json, Forecast::class.java)
        }
    }
}