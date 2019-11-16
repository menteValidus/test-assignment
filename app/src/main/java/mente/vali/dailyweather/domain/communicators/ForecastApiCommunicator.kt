package mente.vali.dailyweather.domain.communicators

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

/**
 * Класс-singleton, предназначенный для работы с запросами к погодному API.
 */
class ForecastApiCommunicator constructor(applicationContext: Context) {
    /**
     * Свойство, обрабатывающее все входящие запросы к API.
     */
    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(applicationContext)
    }
    /**
     * ID текущего города.
     */
    private var cityID = "484907"


    fun requestForecast(listener: Response.Listener<JSONObject>) {
        // TODO сделать Toast уведомления для удобства.
        val request = JsonObjectRequest(
            Request.Method.GET,
            generateApiUrl(),
            null,
            Response.Listener { response ->
                Log.d("test", "Error! ${response}")
            },
            Response.ErrorListener {
                Log.d("test", "Error! ${it.message}")
            }
        )

        add(request)
    }

    /**
     * Метод, добавляющий [request] к [requestQueue].
     */
    private fun <T> add(request: Request<T>) {
        requestQueue.add(request)
    }

    /**
     * Метод, генерирующий URL для запроса к API.
     */
    fun generateApiUrl() =
        "http://api.openweathermap.org/data/2.5/forecast?id=$cityID&units=${UNITS}" +
                "&APPID=${API_KEY}\""

    companion object {

        /**
         * Объект класса.
         */
        @Volatile
        private var INSTANCE: ForecastApiCommunicator? = null

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
                    ?: ForecastApiCommunicator(context).also {
                        INSTANCE = it
                    }
            }
    }
}