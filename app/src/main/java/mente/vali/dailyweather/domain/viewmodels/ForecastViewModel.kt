package mente.vali.dailyweather.domain.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import kotlinx.coroutines.launch
import mente.vali.dailyweather.data.models.Forecast
import mente.vali.dailyweather.data.models.ObservableWeather
import mente.vali.dailyweather.domain.communicators.ForecastApiCommunicator
import mente.vali.dailyweather.domain.repositories.TodayWeatherRepository

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
            acceptCurrentUnits()
//            update()
            //currentFragment.updateUI(currentWeather)
        }

    /**
     *
     */
    private var _currentWeather =
        MutableLiveData<ObservableWeather>().apply {
            value = ObservableWeather()
        }

    private val _temperature = MutableLiveData(16.toShort())
    val temperature: LiveData<Short> = _temperature


    /**
     * Погода на текущее время.
     */
    val currentWeather: LiveData<ObservableWeather> = _currentWeather

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
        acceptCurrentUnits()
    }


    /**
     * Метод, передающий текущие единицы измерения в репозиторий.
     */
    fun saveCurrentUnits() {
        weatherRepository.saveSelectedUnit(currentUnits.getUnitString())
    }

    /**
     * Wrapper для запроса данных с сервера API.
     */
    fun update() = viewModelScope.launch {
        forecastApiCommunicator.requestWeatherByNow(
            Response.Listener { response ->
                _currentWeather.value = ObservableWeather.parse(response.toString())
                _temperature.value = ((_temperature.value ?: 0) + 1).toShort()
            }
        )
        forecastApiCommunicator.requestForecast(
            Response.Listener { response ->
                forecastsList.value = Forecast.parse(response.toString())
            })
    }

    fun increase() {
        _temperature.value = ((_temperature.value ?: 0) + 1).toShort()
    }

    /**
     * Метод, передающий текущие единицы исзмерения в коммуникатор для модификации http-запроса.
     */
    private fun acceptCurrentUnits() {
        forecastApiCommunicator.units = currentUnits.getUnitString()
    }

    /**
     * Единицы градуса.
     */
    enum class Units {
        CELSIUS {
            override fun getString() = "C"

            override fun getUnitString() = "metric"

        },
        FAHRENHEIT {
            override fun getString() = "F"

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
                    CELSIUS.getUnitString() -> CELSIUS
                    FAHRENHEIT.getUnitString() -> FAHRENHEIT
                    else -> CELSIUS
                }
            }
        }
    }
}
