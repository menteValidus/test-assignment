package mente.vali.dailyweather.domain.viewmodels

import android.app.Application
import android.content.SharedPreferences
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Response
import kotlinx.coroutines.launch
import mente.vali.dailyweather.domain.extensions.presentationDateTimeFormat
import mente.vali.dailyweather.data.models.DayWeather
import mente.vali.dailyweather.data.models.Forecast
import mente.vali.dailyweather.data.models.ObservableWeather
import mente.vali.dailyweather.data.models.WeatherByTime
import mente.vali.dailyweather.domain.communicators.ForecastApiCommunicator
import mente.vali.dailyweather.domain.repositories.SharedRepository
import java.util.*

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

    private val preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener
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
    private val _currentWeather: MutableLiveData<ObservableWeather>
    /**
     * Погода на текущее время.
     */
    val currentWeather: LiveData<ObservableWeather>

    private val _tomorrowWeather: MutableLiveData<DayWeather> = MutableLiveData(DayWeather())

    val tomorrowWeather: LiveData<DayWeather> = _tomorrowWeather

    /**
     * Список всех 3-часовых прогнозов на 5 дней.
     */
    private val _daysWeatherList: MutableLiveData<List<Pair<Int, DayWeather>>> =
        MutableLiveData(listOf())
    /**
     *
     */
    val daysWeatherList: MutableLiveData<List<Pair<Int, DayWeather>>> = _daysWeatherList

    private val _selectedCity: MutableLiveData<String> = MutableLiveData(sharedRepository.getCity())

    val selectedCity: LiveData<String> = _selectedCity

    private val _dateTimeOfLastUpdate: MutableLiveData<String>

    val dateTimeOfLastUpdate: LiveData<String>

    var currentScreenType: ScreenType = ScreenType.TODAY

    init {
        // TODO move to SharedRepository
        preferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key.equals("UNITS")) {
                    val unitsPref = sharedPreferences.getString(key, "metric")!!
                    _currentUnitsLiveData.value = Units.get(unitsPref)
                }
            }

        sharedRepository.registerListener(preferenceChangeListener)

        val (date, lastWeather) =
            sharedRepository.getWeatherDate() ?: "" to ObservableWeather()

        if (date == "") {
            // TODO hide UI via LiveData bool value binded to isActive property in XML-layout.
        }

        _currentWeather = MutableLiveData(lastWeather)
        currentWeather = _currentWeather
        _dateTimeOfLastUpdate = MutableLiveData(date)
        dateTimeOfLastUpdate = _dateTimeOfLastUpdate
        acceptCurrentUnits()
    }

    fun setCity(city: String) {
        _selectedCity.value = city
        sharedRepository.saveCity(city)
        forecastApiCommunicator.setCityID(city)
        update()
    }

    /**
     * Метод, передающий данные текущей погоды в репозиторий.
     */
    fun saveCurrentWeather() {
        sharedRepository.saveWeatherData(_dateTimeOfLastUpdate.value!!, currentWeather.value!!)
    }

    // region Binding Methods

    private fun rememberDateTimeOfUpdate() {
        val date = Date().presentationDateTimeFormat()
        _dateTimeOfLastUpdate.value = date

    }

    // endregion

    /**
     * Wrapper для запроса данных с сервера API.
     */
    fun update() = viewModelScope.launch {

        // Если отерыт экран погоды на сегодня, то будет запрос погоды только на сегодня.
        if (currentScreenType == ScreenType.TODAY) {
            forecastApiCommunicator.requestWeatherByNow(
                currentUnitsLiveData.value?.getUnitString(),
                Response.Listener { response ->
                    val weather = ObservableWeather.parse(response.toString())
                    _currentWeather.value!!.setValues(weather)
                    rememberDateTimeOfUpdate()
                }
            )
        } else {
            forecastApiCommunicator.requestForecast(
                currentUnitsLiveData.value?.getUnitString(),
                Response.Listener { response ->
                    // Получаем список всех прогнозов.
                    val list = Forecast.parse(response.toString())
                    val daysWeatherMutableList =
                        mutableListOf<Pair<Int, DayWeather>>()

                    // Проходим по полученному списку, собираем средние данные по дням.
                    for (i in 0..4) {
                        val weatherList = mutableListOf<WeatherByTime>()
                        for (j in 0..7) {
                            // i - номер дня.
                            // j - номер 3-часового отчёта.
                            weatherList.add(list.weatherByTimeList[i * 8 + j])
                        }
                        daysWeatherMutableList.add(i to DayWeather(weatherList))
                    }
                    // Погода на завтра.
                    _tomorrowWeather.value = daysWeatherMutableList[1].second
                    // Погода на 5 дней.
                    daysWeatherList.value = daysWeatherMutableList
                })
        }
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

    /**
     * Перечисление для определения текущего экрана приложения.
     */
    enum class ScreenType {
        TODAY,
        TOMORROW,
        FIVE_DAYS
    }
}
