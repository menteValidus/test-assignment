package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_today_weather.*

import mente.vali.dailyweather.R
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel

/**
 * Подкласс [Fragment], представляющий информацию о погоде на сегодня.
 */
class TodayWeatherFragment : Fragment() {

    /**
     * Поле [ForecastViewModel] для работы с данными, получаемыми от API.
     */
    private val forecastViewModel: ForecastViewModel by lazy {
        ViewModelProviders.of(this).get(ForecastViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Обработка нажатию системной кнопки "Back".
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                activity?.finish()
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_today_weather, container, false)
    }


    override fun onStart() {
        super.onStart()

        updateUI()
        // Переход между фрагментами.
        btn_slide_tomorrow.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_todayWeatherFragment_to_tomorrowWeatherFragment)
        }


    }

    /**
     * Метод, делегирующий обновления UI ViewModel.
     */
    private fun updateUI() {
        forecastViewModel.requestUIUpdate { forecastLiveData, index ->
            forecastLiveData.observe(this, Observer { forecastList ->
                with(forecastList.weatherByTimeList[index]) {
                    // Строка с названием погоды.
                    tw_condition.text = weatherCondition.name
                    // Строка с текущей температурой.
                    // %1$d°%2$s
                    tw_current_temp.text = String.format(
                        resources.getString(R.string.current_temp_placeholder),
                        temperature, forecastViewModel.currentUnits.getString()
                    )
                    // Строка с максимальной и минимальной температурами.
                    // Днём %1$d°%2$s, Ночью %3$d°%4$s
                    tw_maxmin_temp.text = String.format(
                        resources.getString(R.string.maxmin_temp_placeholder),
                        maxTemperature, forecastViewModel.currentUnits.getString(),
                        minTemperature, forecastViewModel.currentUnits.getString()
                    )
                }
            })
        }
    }

}
