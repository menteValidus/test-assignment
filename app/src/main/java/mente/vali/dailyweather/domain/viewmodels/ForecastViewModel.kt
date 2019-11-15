package mente.vali.dailyweather.domain.viewmodels

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

//TODO реализовать ViewModel
/**
 * Класс-singleton для работы с запросами к погодному API.
 */
class ForecastViewModel constructor(context: Context) {
    /**
     * Свойство, обрабатывающее все входящие запросы к API.
     */
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
    /**
     * ID текущего города.
     */
    private var cityID = "484907"
    /**
     * URL для запроса к API.
     */
    private var apiCallUrl: String = generateApiUrl()

    /**
     * Метод, добавляющий [request] к [requestQueue].
     */
    fun <T> add(request: Request<T>) {
        requestQueue.add(request)
    }

    /**
     * Метод, генерирующий URL для запроса к API.
     */
    fun generateApiUrl() =
        "http://api.openweathermap.org/data/2.5/forecast?id=$cityID&units=$UNITS&APPID=$API_KEY\""

    companion object {

        /**
         * Объект класса.
         */
        @Volatile
        private var INSTANCE: ForecastViewModel? = null

        /**
         * Константа, определяющая, какая система мер используется при вызове к API.
         */
        private const val UNITS = "metric"
        /**
         * Константа, хранящая ключ доступа к API.
         */
        private const val API_KEY = "786e5cb5022e6e9b4274c82575c4aea4"

        /**
         * Статический метод, возвращающий объект класса.
         */
        fun getInstance(context: Context) =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: ForecastViewModel(context).also {
                    INSTANCE = it
                }
            }
    }

}