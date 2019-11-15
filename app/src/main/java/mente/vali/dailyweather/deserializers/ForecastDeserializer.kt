package mente.vali.dailyweather.deserializers

import com.google.gson.*
import mente.vali.dailyweather.models.Forecast
import mente.vali.dailyweather.models.WeatherByTime
import mente.vali.dailyweather.models.WeatherCondition
import java.lang.reflect.Type

/**
 * Класс для конвертации JSON в объект класса [Forecast].
 */
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
            weatherByTimeJson as JsonObject

            // Уровень вложенности JSON - list.dt
            val date = weatherByTimeJson.get("dt").asLong
            // Уровень вложенности JSON - list.main
            val main = weatherByTimeJson.getAsJsonObject("main")

            val temperature = main.get("temp").asShort
            val minTemperature = main.get("temp_min").asShort
            val maxTemperature = main.get("temp_max").asShort
            val pressure = main.get("pressure").asInt
            val humidity = main.get("humidity").asShort

            // Уровень вложенности JSON - list.weather
            val weather = weatherByTimeJson.get("weather")

            val condition: WeatherCondition

            // Информация о текущем состоянии погоды предоставляется в виде массива из 1 элемента.
            // Информация также предоставляется в виде массива?
            if (weather.toString()[0] == '[') {
                weather as JsonArray

                condition = WeatherCondition.get(weather[0].asJsonObject.get("main").asString)
            } else {    // Иначе представляем информацию в виде JsonObject.
                weather as JsonObject

                condition = WeatherCondition.get(weather.get("main").asString)
            }

            // Уровень вложенности JSON - list.clouds
            val clouds = weatherByTimeJson.getAsJsonObject("clouds")

            val cloudiness = clouds.get("all").asShort

            // Уровень вложенности JSON - list.wind
            val wind = weatherByTimeJson.getAsJsonObject("wind")

            val speed = wind.get("speed").asInt
            val degrees = wind.get("deg").asShort

            // Уровень вложенности JSON - list.rain
            val rain: JsonObject? = weatherByTimeJson.getAsJsonObject("rain")

            // От API, в случае отсутствия дождей не приходит информация об объёмах осадков.
            val rainVolume = rain?.get("3h")?.asInt

            // Уровень вложенности JSON - list.snow
            val snow: JsonObject? = weatherByTimeJson.getAsJsonObject("snow")

            // От API, в случае отсутствия снегопада не приходит информация об объёмах осадков.
            val snowVolume = snow?.get("3h")?.asInt

            weatherByTimeList.add(
                WeatherByTime(
                    date, temperature, minTemperature, maxTemperature, pressure, humidity,
                    condition, cloudiness, speed, degrees, rainVolume, snowVolume
                )
            )
        }

        return Forecast(weatherByTimeList)
    }
}