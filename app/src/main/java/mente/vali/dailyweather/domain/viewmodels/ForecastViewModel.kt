package mente.vali.dailyweather.domain.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import kotlinx.coroutines.launch
import mente.vali.dailyweather.data.models.Forecast
import mente.vali.dailyweather.data.models.ObservableWeather
import mente.vali.dailyweather.data.models.WeatherByTime
import mente.vali.dailyweather.domain.communicators.ForecastApiCommunicator
import mente.vali.dailyweather.domain.repositories.TodayWeatherRepository
import mente.vali.dailyweather.presentation.ui.fragments.base.BaseFragment
import mente.vali.dailyweather.utils.Utils
import java.util.*

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
            update()
            //currentFragment.updateUI(currentWeather)
        }

    private lateinit var currentFragment: BaseFragment
    /**
     * Погода текущих 3 часов.
     */
    private var _currentWeather: ObservableWeather? = null

    /**
     * Свойство для работы с текущей погодой.
     */
    var currentWeather: ObservableWeather?
        get() {
            return forecastsList.value?.todayForecast
        }
        private set(value) {
            _currentWeather = value
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
        acceptCurrentUnits()
        update()
    }


    /**
     * Метод, передающий текущие единицы исзмерения в репозиторий.
     */
    fun saveCurrentUnits() {
        weatherRepository.saveSelectedUnit(currentUnits.getUnitString())
    }

    /**
     * Метод для обновления UI из Activity.
     */
    fun requestUIUpdate(
        setUI: (ForecastViewModel) -> Unit
    ) {
        viewModelScope.launch {
            // Установить, какой фрагмент обратился, с целью дальнейшего обновления его UI.
            forecastApiCommunicator.requestForecast(
                Response.Listener { response ->
                    forecastsList.value = Forecast.parse(response.toString())
                    setUI(this@ForecastViewModel)
                })
        }
    }

    /**
     * Wrapper для запроса данных с сервера API.
     */
    private fun update() = viewModelScope.launch {
        forecastApiCommunicator.requestForecast(
            Response.Listener { response ->
                forecastsList.value = Forecast.parse(response.toString())
            })
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
