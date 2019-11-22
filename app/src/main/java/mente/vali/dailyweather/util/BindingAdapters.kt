package mente.vali.dailyweather.util

import android.view.View
import android.widget.*
import androidx.databinding.BindingAdapter
import mente.vali.dailyweather.R
import mente.vali.dailyweather.data.models.WeatherCondition
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import androidx.appcompat.widget.AppCompatSpinner
import androidx.databinding.InverseBindingAdapter
import android.widget.ArrayAdapter
import android.widget.AdapterView
import androidx.databinding.InverseBindingListener



/**
 * TODO
 */
@BindingAdapter(value = ["app:degreesUnit", "app:temperature"], requireAll = true)
fun setTemperatureString(textView: TextView, degreesUnit: ForecastViewModel.Units, temperature: Short) {
    textView.text = textView.context.resources.getString(R.string.current_temp_placeholder, temperature.toInt(), degreesUnit.getString())
}

@BindingAdapter(value = ["app:degreesUnit", "app:temperature"], requireAll = true)
fun setTemperatureDrawable(imageView: ImageView, degreesUnit: ForecastViewModel.Units, temperature: Short) {
    when (degreesUnit) {
        ForecastViewModel.Units.CELSIUS -> {
            if (temperature > 0) {
                imageView.setImageResource(R.drawable.ic_over_zero_temp)
            } else {
                imageView.setImageResource(R.drawable.ic_under_zero_temp)
            }
        }
        ForecastViewModel.Units.FAHRENHEIT -> {
            if (temperature > 32) {
                imageView.setImageResource(R.drawable.ic_over_zero_temp)
            } else {
                imageView.setImageResource(R.drawable.ic_under_zero_temp)
            }
        }
    }
}

@BindingAdapter("app:currentDegreesUnit")
fun setActiveRadioButton(radioGroup: RadioGroup, currentDegreesUnit: ForecastViewModel.Units) {
    // Получить context для доступа к ресурсам.
    val ctx = radioGroup.context
    val childCount = radioGroup.childCount

    if (childCount == 0) return

    for (i in 0..radioGroup.childCount) {
        val view = radioGroup.getChildAt(i)

        if (view !is RadioButton) return
        // Проверяем дочерние RadioButton. Устанавливаем тот, который соответствует текущей единице.
        when(view.text) {
            ctx.getString(R.string.celsius_degree_string) -> {
                // TODO изменить на условие.
                when(currentDegreesUnit) {
                    ForecastViewModel.Units.CELSIUS -> view.isChecked = true
                }
            }
            ctx.getString(R.string.fahrenheit_degree_string) -> {
                when(currentDegreesUnit) {
                    ForecastViewModel.Units.FAHRENHEIT -> view.isChecked = true
                }
            }
        }
    }
}

@BindingAdapter("app:weatherCondition")
fun setWeatherCondtitionDrawable(imageView: ImageView, weatherCondition: WeatherCondition) {
    when (weatherCondition) {
        WeatherCondition.CLEAR -> imageView.setImageResource(R.drawable.ic_clear)
        WeatherCondition.CLOUDS -> imageView.setImageResource(R.drawable.ic_cloudy)
        WeatherCondition.RAIN -> imageView.setImageResource(R.drawable.ic_rain)
        WeatherCondition.THUNDERSTORM -> imageView.setImageResource(R.drawable.ic_storm)
        WeatherCondition.SNOW -> imageView.setImageResource(R.drawable.ic_snowing)
        WeatherCondition.MIST -> imageView.setImageResource(R.drawable.ic_fog)
        WeatherCondition.NONE -> imageView.setImageResource(R.drawable.ic_clear)
    }
}
@BindingAdapter("app:weatherCondition")
fun setWeatherCondtitionText(textView: TextView, weatherCondition: WeatherCondition) {
    textView.text = weatherCondition.getConditionName()
}


//////////////////////////////////////////////////////////////////////
object SpinnerBindingUtils {
    @BindingAdapter(
        value = ["selectedValue", "selectedValueAttrChanged"],
        requireAll = false
    )
    fun bindSpinnerData(
        pAppCompatSpinner: AppCompatSpinner,
        newSelectedValue: String?,
        newTextAttrChanged: InverseBindingListener
    ) {
        pAppCompatSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                newTextAttrChanged.onChange()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        if (newSelectedValue != null) {
            val pos =
                (pAppCompatSpinner.adapter as ArrayAdapter<String>).getPosition(newSelectedValue)
            pAppCompatSpinner.setSelection(pos, true)
        }
    }

    @InverseBindingAdapter(
        attribute = "selectedValue",
        event = "selectedValueAttrChanged"
    )
    fun captureSelectedValue(pAppCompatSpinner: AppCompatSpinner): String {
        return pAppCompatSpinner.selectedItem as String
    }
}
