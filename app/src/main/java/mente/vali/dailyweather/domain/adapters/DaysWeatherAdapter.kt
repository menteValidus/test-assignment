package mente.vali.dailyweather.domain.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import mente.vali.dailyweather.R
import mente.vali.dailyweather.data.models.DayWeather
import mente.vali.dailyweather.databinding.ListItemBinding
import mente.vali.dailyweather.domain.adapters.base.DataBoundListAdapter
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel

class DaysWeatherAdapter(private val viewModel: ForecastViewModel) : DataBoundListAdapter<DayWeather>(
    diffCallback = object : DiffUtil.ItemCallback<DayWeather>() {
        override fun areContentsTheSame(oldItem: DayWeather, newItem: DayWeather): Boolean {
            return false
        }

        override fun areItemsTheSame(oldItem: DayWeather, newItem: DayWeather): Boolean {
            return false
        }
    }
) {
    override fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding {
        return ListItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    }

    override fun bind(binding: ViewDataBinding, item: DayWeather) {
        when (binding) {
            is ListItemBinding -> {
                binding.weather = item
            }
        }
    }

}