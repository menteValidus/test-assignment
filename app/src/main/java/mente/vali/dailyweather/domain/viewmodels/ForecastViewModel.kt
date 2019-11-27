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
import mente.vali.dailyweather.domain.extensions.isSameDay
import mente.vali.dailyweather.domain.repositories.SharedRepository
import mente.vali.dailyweather.util.displayMessage
import mente.vali.dailyweather.util.parseDate
import java.util.*

/**
 * Основная ViewModel приложения.
 */
class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Context приложения.
     * Применяется для вывода сообщений об ошибках.
     */
    private val appContext = application.applicationContext

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
     * Текущие единицы измерения градуса.
     */
    private val _currentUnitsLiveData: MutableLiveData<Units> =
        MutableLiveData(Units.get(sharedRepository.getSelectedUnit()))
    /**
     * Свойство, представляющее поле [_currentUnitsLiveData].
     */
    val currentUnitsLiveData: LiveData<Units> = _currentUnitsLiveData

    /**
     * Погода на текущее время.
     */
    private val _currentWeather: MutableLiveData<ObservableWeather>
    /**
     * Свойство, представляющее поле [_currentWeather].
     */
    val currentWeather: LiveData<ObservableWeather>

    /**
     * Погода на завтра.
     */
    private val _tomorrowWeather: MutableLiveData<DayWeather> = MutableLiveData(DayWeather())
    /**
     * Свойство, представляющее поле [_tomorrowWeather].
     */
    val tomorrowWeather: LiveData<DayWeather> by lazy {
        _isDataUnprepared.value = true
        update()
        _tomorrowWeather
    }

    /**
     * Список всех 3-часовых прогнозов на 5 дней.
     * Формат хранимых данных = Список<Пар<Дневного сдвига И Средней погоды на день>>
     */
    private val _daysWeatherList: MutableLiveData<List<Pair<Int, DayWeather>>> =
        MutableLiveData(listOf())
    /**
     * Свойство, представляющее поле [_daysWeatherList].
     */
    val daysWeatherList: MutableLiveData<List<Pair<Int, DayWeather>>> = _daysWeatherList

    /**
     * Текущий город, по которому собираются все данные.
     */
    private val _selectedCity: MutableLiveData<String> = MutableLiveData(sharedRepository.getCity())
    /**
     * Свойство, представляющее поле [_selectedCity].
     */
    val selectedCity: LiveData<String> = _selectedCity

    /**
     * Время последнего обновления текущей погоды.
     */
    private val _dateTimeOfLastUpdate: MutableLiveData<String>
    /**
     * Свойство, представляющее поле [_dateTimeOfLastUpdate].
     */
    val dateTimeOfLastUpdate: LiveData<String>

    /**
     * Поле, показывающее являются ли данные для текущего экрана неподходящими.
     * В случае, если при первом запуске
     */
    private val _isFetching: MutableLiveData<Boolean> = MutableLiveData(false)
    /**
     * Свойство, представляющее поле [_isFetching].
     */
    val isFetching: LiveData<Boolean> = _isFetching

    /**
     * Флаг, устанавливаемый, если данные ещё неинициализированы.
     * Например, при смене города данные старого города будут отображаться пока не придут новые.
     */
    private val _isDataUnprepared = MutableLiveData(false)
    /**
     * Свойство, представляющее поле [_isFetching].
     */
    val isDataUnprepared: LiveData<Boolean> = _isDataUnprepared

    /**
     * Поле, хранящее данные о текущем активном экране.
     */
    var currentScreenType: ScreenType = ScreenType.TODAY

    /**
     * Поле для прослушивания изменений в репозитории.
     */
    private val preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener

    init {
        // Прослушивание изменений текущих единиц измерения - C/F.
        preferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                if (key == "UNITS") {
                    val unitsPref = sharedPreferences.getString(key, "metric")!!
                    _currentUnitsLiveData.value = Units.get(unitsPref)
                    _isDataUnprepared.value = true
                    update()
                }
            }
        sharedRepository.registerListener(preferenceChangeListener)

        // Получение последних данных о текущей погоде.
        val (date, lastWeather) =
            sharedRepository.getWeatherDate() ?: "" to ObservableWeather()

        // Если полученный из SharedPreferences прогноз не на сегодня, то скрывакм UI, пока не
        // приду новые данные с сервера.
        if (date == "" || !Date().isSameDay(parseDate(date))) {
            _isDataUnprepared.value = true
        }

        _currentWeather = MutableLiveData(lastWeather)
        currentWeather = _currentWeather
        _dateTimeOfLastUpdate = MutableLiveData(date)
        dateTimeOfLastUpdate = _dateTimeOfLastUpdate
        acceptCurrentUnits()

        update()
    }

    /**
     * Метод, устанавливающий в системе текущий город.
     */
    fun setCity(city: String) {
        _selectedCity.value = city
        sharedRepository.saveCity(city)
        forecastApiCommunicator.setCityID(city)
        // При смене города данные становятся неактуальны.
        _isDataUnprepared.value = true
        update()
    }

    /**
     * Метод, передающий данные текущей погоды в репозиторий.
     */
    fun saveCurrentWeather() {
        sharedRepository.saveWeatherData(_dateTimeOfLastUpdate.value!!, currentWeather.value!!)
    }

    /**
     * Метод, сохраняющий текущее время в поле [_dateTimeOfLastUpdate].
     * Сохраняется время последнего сохранения.
     */
    private fun rememberDateTimeOfUpdate() {
        val date = Date().presentationDateTimeFormat()
        _dateTimeOfLastUpdate.value = date

    }

    /**
     * Wrapper для запроса данных с сервера API.
     */
    fun update() = viewModelScope.launch {
        // Сообщаем, что идёт загрузка данных
        _isFetching.value = true
        // Если отерыт экран погоды на сегодня, то будет запрос погоды только на сегодня.
//        if (currentScreenType == ScreenType.TODAY) {
        forecastApiCommunicator.requestWeatherByNow(
            currentUnitsLiveData.value?.getUnitString(),
            Response.Listener { response ->
                val weather = ObservableWeather.parse(response.toString())
                _currentWeather.value!!.setValues(weather)
                rememberDateTimeOfUpdate()

                _isFetching.value = false
                // Если данные были неподходящими, то после получения данных необходимо сбросить
                // флаг.
                if (_isDataUnprepared.value!!) {
                    _isDataUnprepared.value = false
                }
            },
            Response.ErrorListener {
                displayMessage(appContext, "Проблемы соединения с сервером.")
                _isFetching.value = false
            }
        )
//        } else {
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

                _isFetching.value = false
                // Если данные были неинициализированными, то после получения данных необходимо
                // сбросить флаг.
                if (_isDataUnprepared.value!!) {
                    _isDataUnprepared.value = false
                }
            },
            Response.ErrorListener {
                displayMessage(appContext, "Проблемы соединения с сервером.")
                _isFetching.value = false
            })
//        }
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
