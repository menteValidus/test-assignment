package mente.vali.dailyweather.data.models

import mente.vali.dailyweather.data.enums.WeatherCondition

/**
 * Класс, предназначенный для хранения всех данных 3-часовых отчётов по погоде.
 *
 * Используется для обработки всех полученных данных из ответа с API
 * */
data class WeatherByTime(
    val date: Long,
    val temperature: Short,
    val minTemperature: Short,
    val maxTemperature: Short,
    val pressure: Int,
    val humidity: Short, // %
    val weatherCondition: WeatherCondition,
    val cloudiness: Short, // %
    val windSpeed: Int,
    val windDegrees: Short,
    val rainVolume: Int?,
    val snowVolume: Int?
)