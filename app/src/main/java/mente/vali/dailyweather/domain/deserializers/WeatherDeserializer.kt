package mente.vali.dailyweather.domain.deserializers

import com.google.gson.*
import mente.vali.dailyweather.data.enums.WeatherCondition
import mente.vali.dailyweather.data.models.WeatherByTime
import java.lang.reflect.Type

/** Класс для конвертации JSON в объект класса [WeatherByTime]. */
class WeatherDeserializer : JsonDeserializer<WeatherByTime> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): WeatherByTime {
        json as JsonObject

        // Уровень вложенности JSON - list.dt
        val date = json.get("dt").asLong * 1000
        // Уровень вложенности JSON - list.main
        val main = json.getAsJsonObject("main")

        val temperature = main.get("temp").asShort
        val minTemperature = main.get("temp_min").asShort
        val maxTemperature = main.get("temp_max").asShort
        val pressure = main.get("pressure").asInt
        val humidity = main.get("humidity").asShort

        // Уровень вложенности JSON - list.weather
        val weather = json.get("weather")

        val condition: WeatherCondition

        // Информация о текущем состоянии погоды предоставляется в виде массива из 1 элемента.
        // Информация также предоставляется в виде массива?
        if (weather.toString()[0] == '[') {
            weather as JsonArray

            condition = WeatherCondition.getByNameFromApi(weather[0].asJsonObject.get("main").asString)
        } else {    // Иначе представляем информацию в виде JsonObject.
            weather as JsonObject

            condition = WeatherCondition.getByNameFromApi(weather.get("main").asString)
        }

        // Уровень вложенности JSON - list.clouds
        val clouds = json.getAsJsonObject("clouds")

        val cloudiness = clouds.get("all").asShort

        // Уровень вложенности JSON - list.wind
        val wind = json.getAsJsonObject("wind")

        val speed = wind.get("speed").asInt
        val degrees = wind.get("deg").asShort

        // Уровень вложенности JSON - list.rain
        val rain: JsonObject? = json.getAsJsonObject("rain")

        // От API, в случае отсутствия дождей не приходит информация об объёмах осадков.
        val rainVolume = rain?.get("3h")?.asInt

        // Уровень вложенности JSON - list.snow
        val snow: JsonObject? = json.getAsJsonObject("snow")

        // От API, в случае отсутствия снегопада не приходит информация об объёмах осадков.
        val snowVolume = snow?.get("3h")?.asInt


        return WeatherByTime(
            date, temperature, minTemperature, maxTemperature, pressure, humidity,
            condition, cloudiness, speed, degrees, rainVolume, snowVolume
        )

    }
}