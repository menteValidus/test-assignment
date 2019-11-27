package mente.vali.dailyweather.presentation.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import mente.vali.dailyweather.data.enums.ScreenType
import mente.vali.dailyweather.databinding.FragmentTomorrowWeatherBinding
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.activities.MainActivity

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
