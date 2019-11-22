package mente.vali.dailyweather.presentation.ui

import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import mente.vali.dailyweather.R
import mente.vali.dailyweather.databinding.ActivityMainBinding
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel

/**
 * Класс Activity, представляющий главный экран приложения.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Поле [ForecastViewModel] для работы с данными, получаемыми от API.
     */
    private val forecastViewModel: ForecastViewModel by lazy {
        ViewModelProviders.of(this).get(ForecastViewModel::class.java)
    }

    /**
     * Метод, вызываемый при запуске Activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)

        binding.lifecycleOwner = this

        binding.viewmodel = forecastViewModel
//        initUI()
    }

    /**
     * Метод, вызываемый при завершении Activity.
     */
    override fun onStop() {
        super.onStop()
        forecastViewModel.saveCurrentUnits()
        // TODO принудительное завершение запросов.
    }

//    /**
//     * Метод, обрабатывающий смену единиц градуса.
//     */
//    fun onRadioButtonClicked(view: View) {
//        if (view is RadioButton) {
//            // Проверяем, является ли кнопка выбранной.
//            val checked = view.isChecked
//
//            // Проверяем, какая кнопка была выбрана.
//            when (view.id) {
//                R.id.rb_celsius -> {
//                    if (checked) {
//                        forecastViewModel.setCurrentUnits(ForecastViewModel.Units.CELSIUS)
//                    }
//                }
//                R.id.rb_fahrenheit -> {
//                    if (checked) {
//                        forecastViewModel.setCurrentUnits(ForecastViewModel.Units.FAHRENHEIT)
//                    }
//                }
//            }
//
//        }
//    }



//    /**
//     * Метод, приводящий весь UI к начальномму положению.
//     */
//    private fun initUI() {
//        val units = forecastViewModel.currentUnitsLiveData.value
//
//        // Установить radiobutton в нужное положение.
//        if (units == ForecastViewModel.Units.FAHRENHEIT) {
//            rb_fahrenheit.isChecked = true
//        } else {
//            rb_celsius.isChecked = true
//        }
//    }


}
