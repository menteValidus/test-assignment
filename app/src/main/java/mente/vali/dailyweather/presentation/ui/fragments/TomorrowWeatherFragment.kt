package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_today_weather.*
import kotlinx.android.synthetic.main.fragment_tomorrow_weather.*

import mente.vali.dailyweather.R

/**
 * A simple [Fragment] subclass.
 */
class TomorrowWeatherFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Обработка нажатию системной кнопки "Back".
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_tomorrowWeatherFragment_pop)
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tomorrow_weather, container, false)
    }

    override fun onStart() {
        super.onStart()
        btn_slide_to_days.setOnClickListener { view ->
            view.findNavController().navigate(R.id.action_tomorrowWeatherFragment_to_fiveDaysFragment)
        }
    }

}
