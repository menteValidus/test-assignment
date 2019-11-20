package mente.vali.dailyweather.data.models

import androidx.lifecycle.MutableLiveData

class ObservableWeather (weatherList: WeatherByTime) {
    val temperature: MutableLiveData<Short> = MutableLiveData()
    val weatherCondtion: MutableLiveData<WeatherCondition> = MutableLiveData()

    init {
        temperature.value = weatherList.temperature
        weatherCondtion.value = weatherList.weatherCondition
    }
}