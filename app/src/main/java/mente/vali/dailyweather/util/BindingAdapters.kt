package mente.vali.dailyweather.util

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import mente.vali.dailyweather.R
import mente.vali.dailyweather.data.enums.Units
import mente.vali.dailyweather.data.enums.WeatherCondition
import mente.vali.dailyweather.domain.extensions.presentationDateWithDayOffsetFormat

/** Адаптер для отображения [temperature] с текущими [degreesUnit] в [textView]. */
@BindingAdapter(value = ["app:degreesUnit", "app:temperature"], requireAll = true)
fun setTemperatureString(
    textView: TextView,
    degreesUnit: Units,
    temperature: Short
) {
    textView.text = textView.context.resources.getString(
        R.string.current_temp_placeholder,
        temperature.toInt(),
        degreesUnit.getString()
    )
}

/**
 * Адаптер для отображения уровня температуры - выше нуля или ниже нуля.
 * В зависимости от [temperature] и [degreesUnit] устанавливается изображение в [imageView].
 **/
@BindingAdapter(value = ["app:degreesUnit", "app:temperature"], requireAll = true)
fun setTemperatureDrawable(
    imageView: ImageView,
    degreesUnit: Units,
    temperature: Short
) {
    when (degreesUnit) {
        Units.CELSIUS -> {
            if (temperature > 0) {
                imageView.setImageResource(R.drawable.ic_over_zero_temp)
            } else {
                imageView.setImageResource(R.drawable.ic_under_zero_temp)
            }
        }
        Units.FAHRENHEIT -> {
            if (temperature > 32) {
                imageView.setImageResource(R.drawable.ic_over_zero_temp)
            } else {
                imageView.setImageResource(R.drawable.ic_under_zero_temp)
            }
        }
    }
}

/**
 * Адаптер для отображения текущей погоды.
 * В зависимости от [weatherCondition] устанавливается изображение в [imageView].
 **/
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

/**
 * Адаптер для текстового отображения текущей погоды.
 * В зависимости от [weatherCondition] устанавливается текст в [textView].
 **/
@BindingAdapter("app:weatherCondition")
fun setWeatherConditionText(textView: TextView, weatherCondition: WeatherCondition) {
    textView.text = weatherCondition.getConditionName()
}

/** Адаптер для текстового отображения [percents] в [textView]. */
@BindingAdapter("app:percentage")
fun setPercentageText(textView: TextView, percents: Short) {
    textView.text = textView.context.resources.getString(R.string.percentage_placeholder, percents)
}

/** Адаптер для текстового отображения [pressure] в [textView]. */
@BindingAdapter("app:pressure")
fun setPressureText(textView: TextView, pressure: Int) {
    textView.text = textView.context.resources.getString(
        R.string.hg_placeholder,
        barToHg(pressure).toString()
    )
}

/**
 * Адаптер для конвертирования [dayOffset] относительно текущей даты в [textView].
 * Если передаётся [dayOffset] = 0, то текст устанавливается "Сегодня".
 * Иначе устанавливается дата в формате "День Месяца", например, "6 ноября".
 **/
@BindingAdapter("app:dayOffset")
fun setDayOffsetText(textView: TextView, dayOffset: Int) {
    if (dayOffset > 0) {
        textView.text = presentationDateWithDayOffsetFormat(dayOffset)
    } else {
        textView.text = "Сегодня"
    }
}

/** Адаптер для текстового отображения [speed] в [textView].. */
@BindingAdapter(value = ["app:degreesUnit", "app:speed"], requireAll = true)
fun setSpeedText(textView: TextView, degreesUnit: Units, speed: Int) {
    if (degreesUnit == Units.FAHRENHEIT) {
        textView.text =
            textView.context.resources.getString(R.string.speed_placeholder, mphToMps(speed))
    } else {
        textView.text = textView.context.resources.getString(R.string.speed_placeholder, speed)
    }
}


