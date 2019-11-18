package mente.vali.dailyweather.domain.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import kotlinx.coroutines.launch
import mente.vali.dailyweather.data.models.Forecast
import mente.vali.dailyweather.domain.communicators.ForecastApiCommunicator
import mente.vali.dailyweather.domain.repositories.TodayWeatherRepository
// TODO добавить комментарии.
/**
 * Основная ViewModel приложения.
 */
class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    /**
     *
     */
    var unit: Units
    /**
     * Список всех 3-часовых прогнозов на 5 дней.
     */
    val forecastsList: MutableLiveData<Forecast> = MutableLiveData()
    /**
     * Свойство для получения объекта класса [ForecastApiCommunicator].
     * Предназначено для общения с сервером API.
     */
    private val forecastApiCommunicator: ForecastApiCommunicator =
        ForecastApiCommunicator.getInstance(application.applicationContext)
    /**
     * Свойство для получения объекта класса [TodayWeatherRepository].
     * Предназначено для хранения данных в локальном хранилище.
     */
    private val todayWeatherRepository: TodayWeatherRepository =
        TodayWeatherRepository.getInstance(application.applicationContext)

    init {
        unit = Units.get(todayWeatherRepository.getSelectedUnit())
        acceptUnits()
        requestUpdate()
    }

    /**
     * Wrapper для запроса данных с сервера API.
     */
    fun requestUpdate() = viewModelScope.launch {
        forecastApiCommunicator.requestForecast(
            Response.Listener { response ->
                forecastsList.value = Forecast.parse(response.toString())
            })
    }

    fun saveUnits() {
        todayWeatherRepository.saveSelectedUnit(unit.getUnitString())
    }

    /**
     *
     */
    fun setUnits(units: Units) {
        this.unit = units
        saveUnits()
        acceptUnits()
    }

    private fun acceptUnits() {
        forecastApiCommunicator.units = unit.getUnitString()
    }

    /**
     * Единицы градуса.
     */
    enum class Units(string: String) {
        CELSIUS("metric") {
            override fun getString() = "&#xb0;C"

            override fun getUnitString() = "metric"

        },
        FAHRENHEIT("imperial") {
            override fun getString() = "&#xb0;F"

            override fun getUnitString() = "imperial"
        };

        /**
         * Получить строку со значениями единицы градуса.
         * Для отображения на экране.
         */
        abstract fun getString(): String

        /**
         * Получить строку со значениями единицы градуса.
         * Для передачи на сервер API.
         */
        abstract fun getUnitString(): String

        companion object {
            /**
             * Статический метод для конвертации строки в [Units].
             * Стандартное значение [Units.CELSIUS].
             */
            fun get(conditionString: String): Units {
                return when (conditionString) {
                    "metric" -> CELSIUS
                    "imperial" -> FAHRENHEIT
                    else -> CELSIUS
                }
            }
        }
    }
}