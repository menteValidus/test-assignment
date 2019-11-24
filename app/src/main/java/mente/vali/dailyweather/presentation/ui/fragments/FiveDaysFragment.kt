package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import kotlinx.android.synthetic.main.fragment_five_days.*

import mente.vali.dailyweather.R
import mente.vali.dailyweather.databinding.FragmentFiveDaysBinding
import mente.vali.dailyweather.domain.adapters.DaysWeatherAdapter
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.ui.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class FiveDaysFragment : Fragment() {
    private lateinit var forecastViewModel: ForecastViewModel
    private lateinit var binding: FragmentFiveDaysBinding
    private lateinit var adapter: DaysWeatherAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Обработка нажатию системной кнопки "Back".
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.action_fiveDaysFragment_pop)
            }

        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        forecastViewModel = (activity as MainActivity).getSharedViewModel()
        // Inflate the layout for this fragment
        binding = FragmentFiveDaysBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = DaysWeatherAdapter(viewModel = forecastViewModel)
        binding.rvDaysWeather.adapter = adapter
        binding.rvDaysWeather.apply {
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

        val list = forecastViewModel.daysWeatherList.value!!
        adapter.submitList(list)
    }

    override fun onStart() {
        super.onStart()

//        val daysWeatherAdapter = DaysWeatherAdapter(context!!, forecastViewModel.daysWeatherList.value!!)
//        rv_days_weather.adapter = daysWeatherAdapter
    }


}
