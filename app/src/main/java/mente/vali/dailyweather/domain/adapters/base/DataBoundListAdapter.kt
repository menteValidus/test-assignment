package mente.vali.dailyweather.domain.adapters.base

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class DataBoundListAdapter<T>(
    diffCallback: DiffUtil.ItemCallback<T>
): ListAdapter<T, DataBoundViewHolder>(
    androidx.recyclerview.widget.AsyncDifferConfig.Builder<T>(diffCallback).build()
) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataBoundViewHolder {
        val binding = createBinding(parent, viewType)
        return DataBoundViewHolder(binding)
    }

    abstract fun createBinding(parent: ViewGroup, viewType: Int): ViewDataBinding

    override fun onBindViewHolder(holder: DataBoundViewHolder, position: Int) {
        if (position < itemCount) {
            bind(holder.binding, getItem(position))
            holder.binding.executePendingBindings()
        }
    }

    abstract fun bind(binding: ViewDataBinding, item: T)

}