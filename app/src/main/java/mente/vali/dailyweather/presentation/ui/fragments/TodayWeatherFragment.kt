package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_today_weather.*

import mente.vali.dailyweather.R
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
        val binding = FragmentTodayWeatherBinding.inflate(inflater, container, false)
//            DataBindingUtil.inflate<FragmentTodayWeatherBinding>(
//                inflater, R.layout.fragment_today_weather,
//                container, false
//            )

//        binding.viewmodel = forecastViewModel
        binding.viewmodel = (activity as MainActivity).getSharedViewModel()
        binding.lifecycleOwner = viewLifecycleOwner

        // TODO пытается получить доступ к ещё неинициализированному списку.
        return binding.root
    }


    override fun onStart() {
        super.onStart()

        //requestAndBindUIData()
        // Переход между фрагментами.
        btn_slide_tomorrow.setOnClickListener { view ->
            view.findNavController()
                .navigate(R.id.action_todayWeatherFragment_to_tomorrowWeatherFragment)
        }

        forecastViewModel.update()


    }


}
