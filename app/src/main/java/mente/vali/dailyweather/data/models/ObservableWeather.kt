package mente.vali.dailyweather.data.models

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import mente.vali.dailyweather.domain.deserializers.WeatherDeserializer

// TODO add comments
class ObservableWeather (temperature: Short, weatherCondition: WeatherCondition) {
    val temperature: MutableLiveData<Short> = MutableLiveData()
    val weatherCondition: MutableLiveData<WeatherCondition> = MutableLiveData()

    init {
        this.temperature.value = temperature
        this.weatherCondition.value = weatherCondition
    }

    constructor() : this(0, WeatherCondition.NONE)
    constructor(weather: WeatherByTime) : this(weather.temperature, weather.weatherCondition)

    fun setValues(weather: ObservableWeather) {
        this.temperature.value = weather.temperature.value
        this.weatherCondition.value = weather.weatherCondition.value
    }

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