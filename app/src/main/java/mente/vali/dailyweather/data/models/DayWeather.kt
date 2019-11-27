package mente.vali.dailyweather.data.models

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

/**
 * Класс для работы со средними значениями целого дня.
 * Стандартный конструктор принимает список из отчётов по погоде на 1 день и инициилизирует поля
 * средними значениями из списка.
 * При вызове пустого конструктора все поля будут проинициализрованы стандартными значениями.
 */
class DayWeather(dayWeatherByTimeList: List<WeatherByTime>) {

    private val _temperature: MutableLiveData<Short>
    val temperature: LiveData<Short>

    private val _weatherCondition: MutableLiveData<WeatherCondition>
    val weatherCondition: LiveData<WeatherCondition>

    private val _humidity: MutableLiveData<Short>
    val humidity: LiveData<Short>

    private val _pressure: MutableLiveData<Int>
    val pressure: LiveData<Int>

    private val _windSpeed: MutableLiveData<Int>
    val windSpeed: LiveData<Int>

    init {
        var tempSum = 0
        var humiditySum = 0
        var pressureSum = 0
        var speedSum = 0

        // Если передан полный прогноз.
        if (dayWeatherByTimeList.size == DAY_CHUNKS) {
            // Получить сумму по всем полям.
            dayWeatherByTimeList.forEach {
                tempSum += it.temperature
                humiditySum += it.humidity
                pressureSum += it.pressure
                speedSum += it.windSpeed
            }
        }
        // Получить средние значения.
        val avgTemperature = (tempSum / DAY_CHUNKS).toShort()
        val avgHumidity = (humiditySum / DAY_CHUNKS).toShort()
        val avgPressure = pressureSum / DAY_CHUNKS
        val avgSpeed = speedSum / DAY_CHUNKS

        _temperature = MutableLiveData(avgTemperature)

        _weatherCondition = if (dayWeatherByTimeList.isNotEmpty()) {
            MutableLiveData(dayWeatherByTimeList[0].weatherCondition)
        } else {
            MutableLiveData(WeatherCondition.NONE)
        }

        _humidity = MutableLiveData(avgHumidity)
        _pressure = MutableLiveData(avgPressure)
        _windSpeed = MutableLiveData(avgSpeed)

        temperature = _temperature
        weatherCondition = _weatherCondition
        humidity = _humidity
        pressure = _pressure
        windSpeed = _windSpeed
    }

    constructor() : this(listOf())

    companion object {
        /** Количество обрабатываемых отчётов по одному дню. */
        private const val DAY_CHUNKS = 8
    }
}