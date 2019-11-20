package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_today_weather.*

import mente.vali.dailyweather.R
import mente.vali.dailyweather.data.models.WeatherByTime
import mente.vali.dailyweather.databinding.FragmentTodayWeatherBinding
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.ui.fragments.base.BaseFragment

/**
 * Подкласс [Fragment], представляющий информацию о погоде на сегодня.
 */
class TodayWeatherFragment : Fragment(), BaseFragment {

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
        val binding =
            DataBindingUtil.inflate<FragmentTodayWeatherBinding>(
                inflater, R.layout.fragment_today_weather,
                container, false
            )

        requestAndBindUIData(binding)
        // TODO пытается получить доступ к ещё неинициализированному списку.
        return binding.root
    }

    private fun bindUI(binding: FragmentTodayWeatherBinding,
    viewModel: ForecastViewModel) {
        binding.todayWeather = viewModel.currentWeather

    }


    override fun onStart() {
        super.onStart()

        //requestAndBindUIData()
        // Переход между фрагментами.
        btn_slide_tomorrow.setOnClickListener { view ->
            view.findNavController()
                .navigate(R.id.action_todayWeatherFragment_to_tomorrowWeatherFragment)
        }


    }

    /**
     * Метод, делегирующий обновления UI ViewModel.
     */
    private fun requestAndBindUIData(binding: FragmentTodayWeatherBinding) {
        forecastViewModel.requestUIUpdate { viewModel ->
            bindUI(binding, viewModel)
//            forecastLiveData.observe(viewLifecycleOwner, Observer { forecastList ->
//                //updateUI(forecastList.weatherByTimeList[index])
//            })
        }
    }

    /**
     * Метод обновляющий весь UI фрагмента.
     */
    override fun updateUI(weatherByTime: WeatherByTime) {
        with(weatherByTime) {
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
    }

}
