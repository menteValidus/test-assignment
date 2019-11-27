package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_today_weather.*
import kotlinx.android.synthetic.main.fragment_tomorrow_weather.*

import mente.vali.dailyweather.R
import mente.vali.dailyweather.data.enums.ScreenType
import mente.vali.dailyweather.databinding.FragmentTodayWeatherBinding
import mente.vali.dailyweather.databinding.FragmentTomorrowWeatherBinding
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.ui.MainActivity
import mente.vali.dailyweather.util.OnSwipeTouchListener

/**
 * Подкласс [Fragment], представляющий информацию о погоде на завтра.
 */
class TomorrowWeatherFragment : Fragment() {

    /**
     * Поле [ForecastViewModel] для работы с данными, получаемыми от API.
     */
    private lateinit var forecastViewModel: ForecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentTomorrowWeatherBinding.inflate(inflater, container, false)
        forecastViewModel = (activity as MainActivity).getSharedViewModel()
        // Сообщаем ViewModel, какой сейчас экран.
        forecastViewModel.currentScreenType = ScreenType.TOMORROW

        forecastViewModel.update()

        // Binding значений.
        binding.weather = forecastViewModel.tomorrowWeather
        binding.currentUnits = forecastViewModel.currentUnitsLiveData

        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

}
