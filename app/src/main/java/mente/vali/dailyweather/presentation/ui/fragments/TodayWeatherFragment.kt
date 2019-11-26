package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_today_weather.*
import mente.vali.dailyweather.databinding.FragmentTodayWeatherBinding
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.ui.MainActivity
import mente.vali.dailyweather.util.OnSwipeTouchListener

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
        forecastViewModel = (activity as MainActivity).getSharedViewModel()
        forecastViewModel.currentScreenType = ForecastViewModel.ScreenType.TODAY
        binding.viewmodel = forecastViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        srl_update_today.setOnRefreshListener {
            forecastViewModel.update()
            srl_update_today.isRefreshing = false
        }

        srl_update_today.setOnTouchListener(object : OnSwipeTouchListener(context) {
            override fun onSwipeLeft() {
                with(activity as MainActivity) {
                    when (forecastViewModel.currentScreenType) {
                        ForecastViewModel.ScreenType.TODAY -> navigateToTomorrow()
                        ForecastViewModel.ScreenType.TOMORROW -> navigateToFiveDays()
                        ForecastViewModel.ScreenType.FIVE_DAYS -> return
                    }
                }
            }

            override fun onSwipeRight() {
                with(activity as MainActivity) {
                    when (forecastViewModel.currentScreenType) {
                        ForecastViewModel.ScreenType.TODAY -> return
                        ForecastViewModel.ScreenType.TOMORROW -> navigateToToday()
                        ForecastViewModel.ScreenType.FIVE_DAYS -> navigateToTomorrow()
                    }
                }
            }
        })

    }


    override fun onPause() {
        super.onPause()
        forecastViewModel.saveCurrentWeather()
    }


}
