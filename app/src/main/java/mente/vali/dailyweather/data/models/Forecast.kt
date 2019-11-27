package mente.vali.dailyweather.data.models

import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import mente.vali.dailyweather.domain.deserializers.ForecastDeserializer
import mente.vali.dailyweather.util.mphToMps
import java.util.*

/**
 * Класс для работы со средними значениями целого дня.
 *
 * Стандартный конструктор принимает [List]<[WeatherByTime]> и присваивает его значение
 * [weatherByTimeList].
 *
 * При вызове пустого конструктора все поля будут проинициализрованы стандартными значениями.
 */
class Forecast(weatherList: List<WeatherByTime>) {
    /** Список всех 3-часовых прогнозов. */
    val weatherByTimeList: List<WeatherByTime> = weatherList

    companion object {
        /** Парсинг полученного [JsonObject] с получением объекта [Forecast]. */
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