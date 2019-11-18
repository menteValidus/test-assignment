package mente.vali.dailyweather.presentation.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import mente.vali.dailyweather.R
import mente.vali.dailyweather.data.models.WeatherByTime
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

        val units = forecastViewModel.currentUnits

        // Установить radiobutton в нужное положение.
        if (units == ForecastViewModel.Units.FAHRENHEIT) {
            rb_fahrenheit.isChecked = true
        } else {
            rb_celsius.isChecked = true
        }

    }

    /**
     * Метод, вызываемый при завершении Activity.
     */
    override fun onStop() {
        super.onStop()
        forecastViewModel.saveCurrentUnits()
        // TODO принудительное завершение запросов.
    }

    /**
     * Метод, обрабатывающий смену единиц градуса.
     */
    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            // Проверяем, является ли кнопка выбранной.
            val checked = view.isChecked

            // Проверяем, какая кнопка была выбрана.
            when (view.id) {
                R.id.rb_celsius -> {
                    if (checked) {
                        forecastViewModel.currentUnits = ForecastViewModel.Units.CELSIUS
                    }
                }
                R.id.rb_fahrenheit -> {
                    if (checked) {
                        forecastViewModel.currentUnits = ForecastViewModel.Units.FAHRENHEIT
                    }
                }
            }
        }
    }


}
