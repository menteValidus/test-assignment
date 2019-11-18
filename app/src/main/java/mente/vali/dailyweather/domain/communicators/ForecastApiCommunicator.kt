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
    /**
     * Поле, определяющее, какая система мер используется при вызове к API.
     */
    var units = "metric"


    /**
     * Метод, производящий запрос информации с сервера.
     */
    fun requestForecast(listener: Response.Listener<JSONObject>) {
        val request = JsonObjectRequest(
            Request.Method.GET,
            generateApiUrl(),
            null,
            listener,
            Response.ErrorListener {
                // TODO сделать Toast уведомления для удобства.
                Log.d("test", "Error! ${it.message}")
            }
        )

        add(request)
    }

    /**
     * Метод, ставящий [request] в очередь на обработку в [requestQueue].
     */
    private fun <T> add(request: Request<T>) {
        requestQueue.add(request)
    }

    /**
     * Метод, генерирующий URL для запроса к API.
     */
    private fun generateApiUrl() =
        "http://api.openweathermap.org/data/2.5/forecast?id=$cityID&units=${units}" +
                "&APPID=${API_KEY}"

    companion object {

        /**
         * Объект класса.
         */
        @Volatile
        private var INSTANCE: ForecastApiCommunicator? = null

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