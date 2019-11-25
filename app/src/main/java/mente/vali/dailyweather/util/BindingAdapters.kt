package mente.vali.dailyweather.util

import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.databinding.BindingAdapter
import mente.vali.dailyweather.R
import mente.vali.dailyweather.data.extensions.presentationDateWithDayOffsetFormat
import mente.vali.dailyweather.data.models.WeatherCondition
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import java.util.*


/**
 * TODO
 */
@BindingAdapter(value = ["app:degreesUnit", "app:temperature"], requireAll = true)
fun setTemperatureString(
    textView: TextView,
    degreesUnit: ForecastViewModel.Units,
    temperature: Short
) {
    textView.text = textView.context.resources.getString(
        R.string.current_temp_placeholder,
        temperature.toInt(),
        degreesUnit.getString()
    )
}

@BindingAdapter(value = ["app:degreesUnit", "app:temperature"], requireAll = true)
fun setTemperatureDrawable(
    imageView: ImageView,
    degreesUnit: ForecastViewModel.Units,
    temperature: Short
) {
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
        when (view.text) {
            ctx.getString(R.string.celsius_degree_string) -> {
                // TODO изменить на условие.
                when (currentDegreesUnit) {
                    ForecastViewModel.Units.CELSIUS -> view.isChecked = true
                }
            }
            ctx.getString(R.string.fahrenheit_degree_string) -> {
                when (currentDegreesUnit) {
                    ForecastViewModel.Units.FAHRENHEIT -> view.isChecked = true
                }
            }
        }
    }
}

@BindingAdapter("app:weatherCondition")
fun setWeatherConditionDrawable(imageView: ImageView, weatherCondition: WeatherCondition) {
    when (weatherCondition) {
        WeatherCondition.CLEAR -> imageView.setImageResource(R.drawable.ic_clear)
        WeatherCondition.CLOUDS -> imageView.setImageResource(R.drawable.ic_cloudy)
        WeatherCondition.RAIN -> imageView.setImageResource(R.drawable.ic_rain)
        WeatherCondition.DRIZZLE -> imageView.setImageResource(R.drawable.ic_rain)
        WeatherCondition.THUNDERSTORM -> imageView.setImageResource(R.drawable.ic_storm)
        WeatherCondition.SNOW -> imageView.setImageResource(R.drawable.ic_snowing)
        WeatherCondition.MIST -> imageView.setImageResource(R.drawable.ic_fog)
        WeatherCondition.NONE -> imageView.setImageResource(R.drawable.ic_clear)
        WeatherCondition.SMOKE -> imageView.setImageResource(R.drawable.ic_fog)
        WeatherCondition.HAZE -> imageView.setImageResource(R.drawable.ic_fog)
        WeatherCondition.DUST -> imageView.setImageResource(R.drawable.ic_fog)
        WeatherCondition.FOG -> imageView.setImageResource(R.drawable.ic_fog)
        WeatherCondition.SAND -> imageView.setImageResource(R.drawable.ic_fog)
        WeatherCondition.ASH -> imageView.setImageResource(R.drawable.ic_fog)
        WeatherCondition.SQUALL -> imageView.setImageResource(R.drawable.ic_storm)
        WeatherCondition.TORNADO -> imageView.setImageResource(R.drawable.ic_storm)
    }
}

@BindingAdapter("app:weatherCondition")
fun setWeatherCondtitionText(textView: TextView, weatherCondition: WeatherCondition) {
    textView.text = weatherCondition.getConditionName()
}

@BindingAdapter("app:percentage")
fun setPercentageText(textView: TextView, percents: Short) {
    textView.text = textView.context.resources.getString(R.string.percentage_placeholder, percents)
}

@BindingAdapter("app:pressure")
fun setPressureText(textView: TextView, pressure: Int) {
    textView.text = textView.context.resources.getString(
        R.string.hg_placeholder,
        barToHg(pressure).toString()
    )
}
@BindingAdapter("app:dayOffset")
fun setDayOffsetText(textView: TextView, dayOffset: Int) {
    if (dayOffset > 0) {
        textView.text = Date().presentationDateWithDayOffsetFormat(dayOffset)
    } else {
        textView.text = "Сегодня"
    }
}

@BindingAdapter(value = ["app:degreesUnit", "app:speed"], requireAll = true)
fun setSpeedText(textView: TextView, degreesUnit: ForecastViewModel.Units, speed: Int) {
    if (degreesUnit == ForecastViewModel.Units.FAHRENHEIT) {
        textView.text =
            textView.context.resources.getString(R.string.speed_placeholder, mphToMps(speed))
    } else {
        textView.text = textView.context.resources.getString(R.string.speed_placeholder, speed)
    }
}


