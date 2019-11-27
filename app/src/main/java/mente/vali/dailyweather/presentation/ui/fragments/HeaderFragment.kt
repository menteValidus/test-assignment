package mente.vali.dailyweather.presentation.ui.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.fragment_header.*

import mente.vali.dailyweather.databinding.FragmentHeaderBinding
import mente.vali.dailyweather.domain.communicators.ForecastApiCommunicator
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.presentation.ui.MainActivity

/**
 * A simple [Fragment] subclass.
 */
class HeaderFragment : Fragment() {
    private lateinit var forecastViewModel: ForecastViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding =
            FragmentHeaderBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        // Получить ViewModel из MainActivity.
        forecastViewModel = (activity as MainActivity).getSharedViewModel()
        // Binding значений.
        binding.viewmodel = forecastViewModel

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        // Инициализация адаптера для отображения списка городов в Spinner.
        val adapter = ArrayAdapter(
            context!!, android.R.layout.simple_spinner_item, ForecastApiCommunicator.getCitiesList()
        )

        sp_cities.adapter = adapter
        // Установка выбранного города из значения полученного из репозитория в ViewModel.
        sp_cities.setSelection(adapter.getPosition(forecastViewModel.selectedCity.value))

        // Сообщать ViewModel о выборе нового города.
        sp_cities.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (parent != null) {
                    forecastViewModel.setCity(parent.getItemAtPosition(position) as String)
                }
            }

        }
    }
}
