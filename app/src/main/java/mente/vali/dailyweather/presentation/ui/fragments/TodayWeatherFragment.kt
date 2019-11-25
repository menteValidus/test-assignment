package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.fragment_today_weather.*
import mente.vali.dailyweather.databinding.FragmentTodayWeatherBinding
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.ui.MainActivity

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
        binding.viewmodel = forecastViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onStart() {
        super.onStart()

        srl_update_today.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener  {
            override fun onRefresh() {
                forecastViewModel.update()
                srl_update_today.isRefreshing = false
            }
        })
    }


    override fun onPause() {
        super.onPause()
        forecastViewModel.saveCurrentWeather()
    }


}
