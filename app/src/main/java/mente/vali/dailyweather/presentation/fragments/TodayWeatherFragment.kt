package mente.vali.dailyweather.presentation.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import mente.vali.dailyweather.data.enums.ScreenType
import mente.vali.dailyweather.databinding.FragmentTodayWeatherBinding
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.activities.MainActivity

/**
 * Подкласс [Fragment], представляющий информацию о погоде на сегодня.
 */
class TodayWeatherFragment : Fragment() {

    /**
     * Поле [ForecastViewModel] для работы с данными, получаемыми от API.
     */
    private lateinit var forecastViewModel: ForecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTodayWeatherBinding.inflate(inflater, container, false)
        // Получить ViewModel из MainActivity.
        forecastViewModel = (activity as MainActivity).getSharedViewModel()
        // Сообщить ViewModel, какой сейчас экран активен.
        forecastViewModel.currentScreenType = ScreenType.TODAY

        // Binding значений.
        binding.lastUpdateTime = forecastViewModel.dateTimeOfLastUpdate
        binding.units = forecastViewModel.currentUnitsLiveData
        binding.weather = forecastViewModel.currentWeather
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }


}
