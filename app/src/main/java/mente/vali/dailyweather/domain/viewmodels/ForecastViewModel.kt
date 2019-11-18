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
     * Текущие единицы измерения градуса.
     */
    private var _currentUnits: Units
    /**
     * Свойство, отвечающее за модификацию поля [_currentUnits].
     */
    var currentUnits: Units
        get() = _currentUnits
        set(value) {
            _currentUnits = value
            // Сообщить репозиториям об изменениях.
            saveCurrentUnits()
            acceptUnits()
        }
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
    private val weatherRepository: TodayWeatherRepository =
        TodayWeatherRepository.getInstance(application.applicationContext)

    init {
        _currentUnits = Units.get(weatherRepository.getSelectedUnit())
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

    fun saveCurrentUnits() {
        weatherRepository.saveSelectedUnit(currentUnits.getUnitString())
    }

    private fun acceptUnits() {
        forecastApiCommunicator.units = currentUnits.getUnitString()
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