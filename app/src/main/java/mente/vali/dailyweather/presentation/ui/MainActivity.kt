package mente.vali.dailyweather.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import mente.vali.dailyweather.R
import mente.vali.dailyweather.data.models.Forecast
import mente.vali.dailyweather.data.models.WeatherByTime
import mente.vali.dailyweather.domain.communicators.ForecastApiCommunicator
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel

/**
 * Класс Activity, представляющий главный экран приложения.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Поле [ForecastViewModel] для работы с данными, получаемыми от API.
     */
    private lateinit var forecastViewModel: ForecastViewModel

    /**
     * Метод, вызываемый при запуске Activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var forecasts: List<WeatherByTime> = listOf()
        forecastViewModel = ViewModelProviders.of(this).get(ForecastViewModel::class.java)
        forecastViewModel.forecastsList.observe(this, Observer { forecastList ->
            forecastList.let { forecasts = it.weatherByTimeList }
        })

    }


    /**
     * Метод, вызываемый при завершении Activity.
     */
    override fun onStop() {
        super.onStop()
        // TODO принудительное завершение запросов.
    }


}
