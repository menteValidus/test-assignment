package mente.vali.dailyweather.domain.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import kotlinx.coroutines.launch
import mente.vali.dailyweather.data.models.Forecast
import mente.vali.dailyweather.domain.communicators.ForecastApiCommunicator

/**
 * Основная ViewModel приложения.
 */
class ForecastViewModel(application: Application) : AndroidViewModel(application) {
    /**
     * Список всех 3-часовых прогнозов на 5 дней.
     */
    val forecastsList: MutableLiveData<Forecast> = MutableLiveData()
    /**
     * Свойство для получения объекта класса [ForecastApiCommunicator].
     */
    private val forecastApiCommunicator: ForecastApiCommunicator =
        ForecastApiCommunicator.getInstance(application.applicationContext)

    init {
        requestUpdate()
    }

    /**
     * Wrapper для запроса данных с сервера API.
     */
    fun requestUpdate() = viewModelScope.launch {
        forecastApiCommunicator.requestForecast(
            Response.Listener { response ->
                forecastsList.value = Forecast.parse(response.toString())
            })
    }
}