package mente.vali.dailyweather.util

import android.widget.TextView
import androidx.databinding.BindingAdapter
import mente.vali.dailyweather.R
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel

@BindingAdapter(value = ["app:degreesUnit", "android:temperature"], requireAll = true)
fun setTemperatureString(textView: TextView, degreesUnit: ForecastViewModel.Units, temperature: Short) {
    textView.text = textView.context.resources.getString(R.string.current_temp_placeholder, temperature.toInt(), degreesUnit.getString())
}