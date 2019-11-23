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
import mente.vali.dailyweather.domain.repositories.SharedRepository
import mente.vali.dailyweather.util.getCurrentDateTime

/**
 * Основная ViewModel приложения.
 */
class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Свойство для получения объекта класса [ForecastApiCommunicator].
     * Предназначено для общения с сервером API.
     */
    private val forecastApiCommunicator: ForecastApiCommunicator =
        ForecastApiCommunicator.getInstance(application.applicationContext)
    /**
     * Свойство для получения объекта класса [SharedRepository].
     * Предназначено для хранения данных в локальном хранилище.
     */
    private val sharedRepository: SharedRepository =
        SharedRepository.getInstance(application.applicationContext)
    /**
     * Список всех 3-часовых прогнозов на 5 дней.
     */
    val forecastsList: MutableLiveData<Forecast> = MutableLiveData()
    /**
     * Текущие единицы измерения градуса.
     */
    private val _currentUnitsLiveData: MutableLiveData<Units> =
        MutableLiveData(Units.get(sharedRepository.getSelectedUnit()))

    /**
     * Свойство, отвечающее за модификацию поля [_currentUnitsLiveData].
     */
    val currentUnitsLiveData: LiveData<Units> = _currentUnitsLiveData
    /**
     * TODO
     */
    private val _currentWeather =
        MutableLiveData<ObservableWeather>().apply {
            value = ObservableWeather()
        }

    /**
     * Погода на текущее время.
     */
    val currentWeather: LiveData<ObservableWeather> = _currentWeather

    private val _selectedCity: MutableLiveData<String> = MutableLiveData(sharedRepository.getCity())

    val selectedCity: LiveData<String> = _selectedCity

    private val _dateTimeOfLastUpdate: MutableLiveData<String> = MutableLiveData(getCurrentDateTime())

    val dateTimeOfLastUpdate: LiveData<String> = _dateTimeOfLastUpdate

    fun setCity(city: String) {
        _selectedCity.value = city
        sharedRepository.saveCity(city)
        forecastApiCommunicator.setCityID(city)
        update()
    }

    init {
        update()
        acceptCurrentUnits()
    }

    /**
     * TODO
     */
    private fun setCurrentUnits(units: Units) {
        _currentUnitsLiveData.value = units
        acceptCurrentUnits()
        saveCurrentUnits()
    }

    /**
     * Метод, передающий текущие единицы измерения в репозиторий.
     */
    private fun saveCurrentUnits() {
        sharedRepository.saveSelectedUnit(currentUnitsLiveData.value!!.getUnitString())
    }

    // region Binding Methods

    fun setCelsiusUnits() {
        setCurrentUnits(Units.CELSIUS)
        update()
    }

    fun setFahrenheitUnits() {
        setCurrentUnits(Units.FAHRENHEIT)
        update()
    }

    private fun rememberDateTimeOfUpdate() {
        _dateTimeOfLastUpdate.value = getCurrentDateTime()
    }

    // endregion

    /**
     * Wrapper для запроса данных с сервера API.
     */
    fun update() = viewModelScope.launch {
        forecastApiCommunicator.requestWeatherByNow(
            Response.Listener { response ->
                rememberDateTimeOfUpdate()
                _currentWeather.value = ObservableWeather.parse(response.toString())
            }
        )
//        forecastApiCommunicator.requestForecast(
//            Response.Listener { response ->
//                forecastsList.value = Forecast.parse(response.toString())
//            })
    }


    /**
     * Метод, передающий текущие единицы исзмерения в коммуникатор для модификации http-запроса.
     */
    private fun acceptCurrentUnits() {
        forecastApiCommunicator.units = currentUnitsLiveData.value!!.getUnitString()
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
