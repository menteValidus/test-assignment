package mente.vali.dailyweather.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import mente.vali.dailyweather.R
import mente.vali.dailyweather.models.Forecast
import mente.vali.dailyweather.requests.WeatherRequest

class MainActivity : AppCompatActivity() {

    val url = "http://api.openweathermap.org/data/2.5/forecast?q=Taganrog,ru&" +
            "APPID=786e5cb5022e6e9b4274c82575c4aea4"
    /**
     * Свойство для получения объекта класса [WeatherRequest].
     */
    val weatherRequest: WeatherRequest
        get() = WeatherRequest.getInstance(applicationContext)

    /**
     * Метод, вызываемый при запуске Activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        requestWeatherUpdate()
//        val jsonObjectRequest = JsonObjectRequest()
    }


    /**
     * Метод, вызываемый при завершении Activity.
     */
    override fun onStop() {
        super.onStop()
        // TODO принудительное завершение запросов.
    }

    /**
     * Метод запроса обновления показаний погоды.
     */
    private fun requestWeatherUpdate() {
        // Запрос строкового запроса по предоставленному URL
        val stringRequest = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                Log.d("test", "Response is: ${Forecast.parse(response.toString())}.")
            },
            Response.ErrorListener {
                Log.d("test", "Error! ${it.message}")
            }
        )

        weatherRequest.add(stringRequest)
    }

}
