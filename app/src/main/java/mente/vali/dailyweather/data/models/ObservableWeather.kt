package mente.vali.dailyweather.data.models

import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import mente.vali.dailyweather.domain.deserializers.WeatherDeserializer
import mente.vali.dailyweather.util.getDateTimeStringFromLong
import mente.vali.dailyweather.util.windDegreeToWindDirection

// TODO add comments
class ObservableWeather (
    temperature: Short,
    weatherCondition: WeatherCondition,
    humidity: Short,
    pressure: Int,
    windDirection: String,
    windSpeed: Int,
    dateTime: String) {
    val temperature: MutableLiveData<Short> = MutableLiveData()
    val weatherCondition: MutableLiveData<WeatherCondition> = MutableLiveData()
    val humidity: MutableLiveData<Short> = MutableLiveData()
    val pressure: MutableLiveData<Int> = MutableLiveData()
    val windDirection: MutableLiveData<String> = MutableLiveData()
    val windSpeed: MutableLiveData<Int> = MutableLiveData()
    val dateTime: MutableLiveData<String> = MutableLiveData()

    init {
        this.temperature.value = temperature
        this.weatherCondition.value = weatherCondition
        this.humidity.value = humidity
        this.pressure.value = pressure
        this.windDirection.value = windDirection
        this.windSpeed.value = windSpeed
        this.dateTime.value = dateTime
    }

    constructor() : this(0, WeatherCondition.NONE, 0, 0, "NONE", 0, "")
    constructor(weather: WeatherByTime) : this
        (weather.temperature,
        weather.weatherCondition,
        weather.humidity,
        weather.pressure,
        windDegreeToWindDirection(weather.windDegrees.toFloat()),
        weather.windSpeed,
        getDateTimeStringFromLong(weather.date))

    fun setValues(weather: ObservableWeather) {
        temperature.value = weather.temperature.value
        weatherCondition.value = weather.weatherCondition.value
        humidity.value = weather.humidity.value
        pressure.value = weather.pressure.value
        windDirection.value = weather.windDirection.value
        windSpeed.value = weather.windSpeed.value
        dateTime.value = weather.dateTime.value
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