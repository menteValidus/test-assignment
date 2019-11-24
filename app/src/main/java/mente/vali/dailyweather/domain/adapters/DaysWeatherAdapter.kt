package mente.vali.dailyweather.domain.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer
import mente.vali.dailyweather.data.models.DayWeather
import mente.vali.dailyweather.databinding.ListItemBinding
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel

/**
 * TODO
 */
class DaysWeatherAdapter(private val viewModel: ForecastViewModel) :
    RecyclerView.Adapter<DaysWeatherAdapter.ViewHolder>() {
    private var items: List<DayWeather> = viewModel.daysWeatherList.value ?: listOf()

    override fun getItemCount(): Int {
        return items.size
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)
        val viewHolder = BindingViewHolder(binding.root, binding)
        binding.lifecycleOwner = viewHolder
        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        if (holder is BindingViewHolder) {
            holder.apply {
                binding.item = item
                binding.degreesUnits = viewModel.currentUnitsLiveData.value
            }
        }
    }

    override fun onViewAttachedToWindow(holder: ViewHolder) {
        super.onViewAttachedToWindow(holder)
        if (holder is BindingViewHolder) {
            holder.markAttach()
        }
    }

    override fun onViewDetachedFromWindow(holder: ViewHolder) {
        super.onViewDetachedFromWindow(holder)
        if (holder is BindingViewHolder) {
            holder.markDetach()
        }
    }

    fun replaceItems(items: List<DayWeather>) {
        this.items = items
        notifyDataSetChanged()
    }

    open inner class ViewHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer

    inner class BindingViewHolder(override val containerView: View, val binding: ListItemBinding) :
        ViewHolder(containerView), LifecycleOwner {
        private val lifecycleRegistry = LifecycleRegistry(this)

        init {
            lifecycleRegistry.currentState = Lifecycle.State.INITIALIZED
        }

        fun markAttach() {
            lifecycleRegistry.currentState = Lifecycle.State.STARTED
        }

        fun markDetach() {
            lifecycleRegistry.currentState = Lifecycle.State.DESTROYED
        }

        override fun getLifecycle(): Lifecycle {
            return lifecycleRegistry
        }
    }
}