package mente.vali.dailyweather.presentation.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import mente.vali.dailyweather.R
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
     * Метод, вызываемый при создании Activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Инициализация shared ViewModel, которая будет использоваться во всём приложении.
        forecastViewModel =
            ViewModelProviders.of(this).get(ForecastViewModel::class.java)

        setContentView(R.layout.activity_main)
    }


    fun getSharedViewModel() = forecastViewModel

}
