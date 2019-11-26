package mente.vali.dailyweather.presentation.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
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
    private lateinit var navController: NavController

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
        navController = nav_host_fragment.findNavController()

        // Прослушивание для определения, когда происходит запрос данных.
        forecastViewModel.isFetching.observe(this, Observer { isFetching ->
            if (isFetching) {
                if (forecastViewModel.isDataUnappropriated) {
                    showProgressView()
                }
            } else {
                if (forecastViewModel.isDataUnappropriated) {
                    hideProgressView()
                }
            }
        })

    }

    /**
     * Отобразить показатель прогресса загрузки данных.
     */
    private fun showProgressView() {
        rl_progress.visibility = VISIBLE
        nav_host_fragment.view?.visibility = GONE
//        bnv_navigation.visibility = GONE
//        window.setFlags(
//            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
//            WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
//        )
    }

    /**
     * Скрыть показатель прогресса загрузки данных.
     */
    private fun hideProgressView() {
        rl_progress.visibility = GONE
        nav_host_fragment.view?.visibility = VISIBLE
//        bnv_navigation.visibility = VISIBLE
//        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
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

    private fun goToToday() {
        if (navController.currentDestination?.id == R.id.tomorrowWeatherFragment) {
            nav_host_fragment.findNavController()
                .navigate(R.id.action_tomorrowWeatherFragment_to_todayWeatherFragment)
        } else if (forecastViewModel.currentScreenType == ForecastViewModel.ScreenType.FIVE_DAYS) {
            nav_host_fragment.findNavController()
                .navigate(R.id.action_fiveDaysFragment_to_todayWeatherFragment)
        }
    }

    private fun goToTomorrow() {
        if (forecastViewModel.currentScreenType == ForecastViewModel.ScreenType.TODAY) {
            nav_host_fragment.findNavController()
                .navigate(R.id.action_todayWeatherFragment_to_tomorrowWeatherFragment)
        } else if (navController.currentDestination?.id == R.id.fiveDaysFragment) {
            nav_host_fragment.findNavController()
                .navigate(R.id.action_fiveDaysFragment_to_tomorrowWeatherFragment)
        }

    }

    private fun goToFiveDays() {
        if (forecastViewModel.currentScreenType == ForecastViewModel.ScreenType.TODAY) {
            nav_host_fragment.findNavController()
                .navigate(R.id.action_todayWeatherFragment_to_fiveDaysFragment)
        } else if (forecastViewModel.currentScreenType == ForecastViewModel.ScreenType.TOMORROW) {
            nav_host_fragment.findNavController()
                .navigate(R.id.action_tomorrowWeatherFragment_to_fiveDaysFragment)
        }
        nav_host_fragment.findNavController()
            .navigate(R.id.fiveDaysFragment)
    }

    fun navigateToToday() {
        bnv_navigation.selectedItemId = R.id.navigation_tomorrow
    }

    fun navigateToTomorrow() {
        bnv_navigation.selectedItemId = R.id.navigation_tomorrow
    }

    fun navigateToFiveDays() {
        bnv_navigation.selectedItemId = R.id.navigation_tomorrow
    }

    fun getSharedViewModel() = forecastViewModel

}
