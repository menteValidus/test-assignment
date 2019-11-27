package mente.vali.dailyweather.data.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import mente.vali.dailyweather.data.enums.WeatherCondition
import mente.vali.dailyweather.domain.deserializers.WeatherDeserializer

/**
 * Класс для работы с показаниями погоды одного отчёта (3 часа).
 *
 * Стандартный конструктор принимает данные всех полей и инициализирует их этими значениями.
 *
 * При передачи в конструктор [WeatherByTime] все поля буду проинициализированы значениями из
 * [WeatherByTime].
 *
 * При вызове пустого конструктора все поля будут проинициализрованы стандартными значениями.
 */
class ObservableWeather(weather: Weather) {
    private val _temperature: MutableLiveData<Short> = MutableLiveData(weather.temperature)
    val temperature: LiveData<Short> = _temperature

    private val _weatherCondition: MutableLiveData<WeatherCondition> =
        MutableLiveData(weather.weatherCondition)
    val weatherCondition: LiveData<WeatherCondition> = _weatherCondition

    private val _humidity: MutableLiveData<Short> = MutableLiveData(weather.humidity)
    val humidity: LiveData<Short> = _humidity

    private val _pressure: MutableLiveData<Int> = MutableLiveData(weather.pressure)
    val pressure: LiveData<Int> = _pressure

    private val _windDirection: MutableLiveData<String> = MutableLiveData(weather.windDirection)
    val windDirection: LiveData<String> = _windDirection

    private val _windSpeed: MutableLiveData<Int> = MutableLiveData(weather.windSpeed)
    val windSpeed: LiveData<Int> = _windSpeed

    /** Возвращает [ObservableWeather] со стандартными значениями полей. */
    constructor() : this(Weather())
    /** Возвращает [ObservableWeather], составленный на основе одного отчёта [WeatherByTime]. */
    constructor(weatherByTime: WeatherByTime) : this(Weather.makeWeather(weatherByTime))

    /** Возвращает [ObservableWeather], составленный на основе списка отчётов [WeatherByTime]. */
    constructor(weatherByTimeList: List<WeatherByTime>) : this(
        Weather.makeDayWeather(
            weatherByTimeList
        )
    )

    /** Установить значения полей на основе [weather]. */
    fun setValues(weather: ObservableWeather) {
        _temperature.value = weather.temperature.value
        _weatherCondition.value = weather.weatherCondition.value
        _humidity.value = weather.humidity.value
        _pressure.value = weather.pressure.value
        _windDirection.value = weather.windDirection.value
        _windSpeed.value = weather.windSpeed.value
    }

    companion object {
        /**
         * Парсинг полученного [JsonObject] в виде [WeatherByTime] с получением объекта
         * [ObservableWeather].
         */
        fun parseSingle(json: String): ObservableWeather {
            val gson = GsonBuilder()
                .registerTypeAdapter(
                    WeatherByTime::class.java,
                    WeatherDeserializer()
                ).create()
            return ObservableWeather(gson.fromJson(json, WeatherByTime::class.java))
        }
    }
}