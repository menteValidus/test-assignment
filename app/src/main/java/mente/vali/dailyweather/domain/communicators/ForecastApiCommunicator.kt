package mente.vali.dailyweather.domain.communicators

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

/** Класс-singleton, предназначенный для работы с запросами к погодному API. */
class ForecastApiCommunicator constructor(applicationContext: Context) {
    /** Свойство, обрабатывающее все входящие запросы к API. */
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(applicationContext)
    }
    /**
     * ID текущего города.
     * Стандартное значение - ID Таганрога.
     */
    private var cityID = "484907"
    /** Поле, определяющее, какая система мер используется при вызове к API. */
    var units = "metric"

    /**
     * Установить ID переданного города [city].
     * Если этот город уже установлен в [cityID], то возвращается true, иначе false.
     **/
    fun setCityID(city: String): Boolean {
        // Если предыдущий город такой же как и новый,
        return if (cityID == citiesIDMap[city]) { // то возвращаем false.
            false
        } else { // иначе true.
            cityID = citiesIDMap[city] ?: "484907"
            true
        }
    }

    /** Метод, производящий запрос прогноза на 5 дней с сервера. */
    fun requestForecast(
        units: String?,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        request(units, generateForecastApiUrl(), responseListener, errorListener)
    }

    /** Метод, производящий запрос текущей погоды с сервера. */
    fun requestWeatherByNow(
        units: String?,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        request(units, generateWeatherApiUrl(), responseListener, errorListener)
    }

    /** Общий метод запроса данных с сервера. */
    private fun request(
        units: String?,
        url: String,
        responseListener: Response.Listener<JSONObject>,
        errorListener: Response.ErrorListener
    ) {
        this.units = units ?: "metric"

        val request = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            responseListener,
            errorListener
        )

        add(request)
    }

    /** Метод, ставящий [request] в очередь на обработку в [requestQueue]. */
    private fun <T> add(request: Request<T>) {
        requestQueue.add(request)
    }

    /** Метод, генерирующий URL для запроса прогноза на 5 дней. */
    private fun generateForecastApiUrl() =
        "http://api.openweathermap.org/data/2.5/forecast?id=$cityID&units=${units}" +
                "&APPID=${API_KEY}"

    /** Метод, генерирующий URL для запроса текущей погоды. */
    private fun generateWeatherApiUrl() =
        "http://api.openweathermap.org/data/2.5/weather?id=$cityID&units=${units}" +
                "&APPID=${API_KEY}"

    companion object {
        /** Словарь ID городов. */
        private val citiesIDMap: Map<String, String> = mapOf(
            "Таганрог" to "484907",
            "Москва" to "524901",
            "Варшава" to "4927854",
            "Сидней" to "2147714",
            "Нью-Йорк" to "5128638"
        )

        /** Объект класса. */
        @Volatile
        private var INSTANCE: ForecastApiCommunicator? = null

        /** Константа, хранящая ключ доступа к API. */
        private const val API_KEY = "786e5cb5022e6e9b4274c82575c4aea4"

        /** Статический метод, возвращающий объект класса. */
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: ForecastApiCommunicator(context).also {
                        INSTANCE = it
                    }
            }

        /** Метод, преобразовывающий Map [citiesIDMap] в List. */
        fun getCitiesList(): List<String> {
            val list = mutableListOf<String>()
            citiesIDMap.forEach {
                list.add(it.key)
            }
            return list
        }
    }


}