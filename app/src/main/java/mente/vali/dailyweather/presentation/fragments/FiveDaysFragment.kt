package mente.vali.dailyweather.presentation.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration

import mente.vali.dailyweather.data.enums.ScreenType
import mente.vali.dailyweather.databinding.FragmentFiveDaysBinding
import mente.vali.dailyweather.domain.adapters.DaysWeatherAdapter
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.activities.MainActivity

/** Подкласс [Fragment], представляющий информацию о погоде на 5 дней. */
class FiveDaysFragment : Fragment() {
    // region Private Fields

    /** Поле [ForecastViewModel] для работы с данными, получаемыми от API. */
    private lateinit var forecastViewModel: ForecastViewModel
    /** Поле [FragmentFiveDaysBinding] для работы с Binding. */
    private lateinit var binding: FragmentFiveDaysBinding
    /** Поле [DaysWeatherAdapter] для работы с RecyclerView в layout. */
    private lateinit var adapter: DaysWeatherAdapter

    // endregion

    // region onMethods

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Получить ViewModel из MainActivity.
        forecastViewModel = (activity as MainActivity).getSharedViewModel()
        // Сообщить ViewModel, какой сейчас экран активен.
        forecastViewModel.currentScreenType = ScreenType.FIVE_DAYS

        binding = FragmentFiveDaysBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initAdapters()
        initUI()
        initObservers()
    }

    // endregion

    // region Initializators

    /** Инициализация всех адаптеров Fragment. **/
    private fun initAdapters() {
        // Инициализировать адаптер для отображения данных в RecyclerView посредством binding.
        adapter = DaysWeatherAdapter(viewModel = forecastViewModel)
    }

    /** Инициализация UI Fragment. **/
    private fun initUI() {
        // Добавить в RecyclerView разделитель между элементами.
        val divider = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        binding.rvDaysWeather.addItemDecoration(divider)
        binding.rvDaysWeather.adapter = adapter
    }

    /** Инициализация всех Observers Fragment. */
    private fun initObservers() {
        // Прослушивать список погоды на 5 дней.
        forecastViewModel.daysWeatherList.observe(viewLifecycleOwner, Observer {
            // Если произошло изменение заменить элементы в адаптере.
            adapter.replaceItems(it)
        })
    }

    // endregion

}
