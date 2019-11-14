package mente.vali.dailyweather.deserializers

import com.google.gson.*
import mente.vali.dailyweather.models.Forecast
import java.lang.reflect.Type

/**
 * Класс для конвертации JSON в объект класса [Forecast].
 */
class ForecastDeserializer: JsonDeserializer<Forecast> {

    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Forecast {
        json as JsonObject

        // Получить json-строку всех прогнозов.
        val forecast = json.get("list") as JsonArray

        for (weatherByTime in forecast) {
            weatherByTime as JsonObject


        }
//        println(forecast.toString())

        return Forecast(arrayListOf(), "", "")
    }
}