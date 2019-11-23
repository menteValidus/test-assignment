package mente.vali.dailyweather.data.models

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import mente.vali.dailyweather.domain.deserializers.WeatherDeserializer
import mente.vali.dailyweather.util.windDegreeToWindDirection

// TODO add comments
class ObservableWeather (
    temperature: Short,
    weatherCondition: WeatherCondition,
    humidity: Short,
    pressure: Int,
    windDirection: String,
    windSpeed: Int) {
    val temperature: MutableLiveData<Short> = MutableLiveData()
    val weatherCondition: MutableLiveData<WeatherCondition> = MutableLiveData()
    val humidity: MutableLiveData<Short> = MutableLiveData()
    val pressure: MutableLiveData<Int> = MutableLiveData()
    val windDirection: MutableLiveData<String> = MutableLiveData()
    val windSpeed: MutableLiveData<Int> = MutableLiveData()

    init {
        this.temperature.value = temperature
        this.weatherCondition.value = weatherCondition
        this.humidity.value = humidity
        this.pressure.value = pressure
        this.windDirection.value = windDirection
        this.windSpeed.value = windSpeed
    }

    constructor() : this(0, WeatherCondition.NONE, 0, 0, "NONE", 0)
    constructor(weather: WeatherByTime) : this
        (weather.temperature,
        weather.weatherCondition,
        weather.humidity,
        weather.pressure,
        windDegreeToWindDirection(weather.windDegrees.toFloat()),
        weather.windSpeed)

    companion object {
        /**
        * Парсинг полученного [JsonObject] с получением объекта [ObservableWeather].
        */
        fun parse(json: String): ObservableWeather {
            val gson = GsonBuilder()
                .registerTypeAdapter(
                    WeatherByTime::class.java,
                    WeatherDeserializer()
                ).create()
            return ObservableWeather(gson.fromJson(json, WeatherByTime::class.java))
        }
    }
}