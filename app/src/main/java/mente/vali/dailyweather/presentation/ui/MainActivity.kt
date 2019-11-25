package mente.vali.dailyweather.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import mente.vali.dailyweather.R
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel

/**
 * Класс Activity, представляющий главный экран приложения.
 */
class MainActivity : AppCompatActivity() {

    /**
     * Поле [ForecastViewModel] для работы с данными, получаемыми от API.
     */
    private lateinit var forecastViewModel: ForecastViewModel

    /**
     * Метод, вызываемый при создании Activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Инициализация shared ViewModel, которая будет использоваться во всём приложении.
        forecastViewModel =
            ViewModelProviders.of(this).get(ForecastViewModel::class.java)

        setContentView(R.layout.activity_main)

        val navigationListener = BottomNavigationView.OnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_today -> {
                    goToToday()
                    true
                }

                R.id.navigation_tomorrow -> {
                    goToTomorrow()
                    true
                }
                R.id.navigation_five_days -> {
                    goToFiveDays()
                    true
                }
                else -> false
            }
        }

        bnv_navigation.setOnNavigationItemSelectedListener(navigationListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.app_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.settings_menu_point -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }

    }

    fun goToToday() {
        nav_host_fragment.findNavController()
            .navigate(R.id.todayWeatherFragment)
    }

    fun goToTomorrow() {
        nav_host_fragment.findNavController()
            .navigate(R.id.tomorrowWeatherFragment)
    }

    fun goToFiveDays() {
        nav_host_fragment.findNavController()
            .navigate(R.id.fiveDaysFragment)
    }

    fun getSharedViewModel() = forecastViewModel

}
