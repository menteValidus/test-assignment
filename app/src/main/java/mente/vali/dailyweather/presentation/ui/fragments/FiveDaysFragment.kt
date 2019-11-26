package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_five_days.*

import mente.vali.dailyweather.R
import mente.vali.dailyweather.databinding.FragmentFiveDaysBinding
import mente.vali.dailyweather.domain.adapters.DaysWeatherAdapter
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.ui.MainActivity
import mente.vali.dailyweather.util.OnSwipeTouchListener

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
        forecastViewModel = (activity as MainActivity).getSharedViewModel()
        forecastViewModel.currentScreenType = ForecastViewModel.ScreenType.FIVE_DAYS
        // Inflate the layout for this fragment
        binding = FragmentFiveDaysBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onStart() {
        super.onStart()

        srl_update_five_days.setOnRefreshListener {
            // TODO change to 5 days update instead
            forecastViewModel.update()
        }

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        adapter = DaysWeatherAdapter(viewModel = forecastViewModel)
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.rvDaysWeather.addItemDecoration(divider)

        binding.rvDaysWeather.adapter = adapter
        val list = forecastViewModel.daysWeatherList.value!!
        adapter.replaceItems(list)

        forecastViewModel.daysWeatherList.observe(viewLifecycleOwner, Observer {
            adapter.replaceItems(it)
        })
    }

}
