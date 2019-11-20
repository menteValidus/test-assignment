package mente.vali.dailyweather.presentation.ui.fragments.base

import mente.vali.dailyweather.data.models.WeatherByTime

interface BaseFragment {
    fun updateUI(weatherByTime: WeatherByTime)
}