package mente.vali.dailyweather.domain.deserializers

import com.google.gson.*
import mente.vali.dailyweather.data.models.Forecast
import mente.vali.dailyweather.data.models.WeatherByTime
import mente.vali.dailyweather.data.models.WeatherCondition
import java.lang.reflect.Type

/** Класс для конвертации JSON в объект класса [Forecast]. */
class ForecastDeserializer : JsonDeserializer<Forecast> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Forecast {
        json as JsonObject

        // Инициализировать изменяемый список для сбора десериализированных значений.
        val weatherByTimeList = mutableListOf<WeatherByTime>()

        // Получить json-строку всех прогнозов.
        val forecastJsonArray = json.get("list") as JsonArray

        // Проход по всем 40 3-часовым отчётам с их десериализацией и добавлением в общий список.
        for (weatherByTimeJson in forecastJsonArray) {

            val gson = GsonBuilder()
                .registerTypeAdapter(
                    WeatherByTime::class.java,
                    WeatherDeserializer()
                ).create()

            weatherByTimeList.add(gson.fromJson(weatherByTimeJson, WeatherByTime::class.java))
        }

        return Forecast(weatherByTimeList)
    }
}