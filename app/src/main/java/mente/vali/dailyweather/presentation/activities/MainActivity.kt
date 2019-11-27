package mente.vali.dailyweather.presentation.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_main.*
import mente.vali.dailyweather.R
import mente.vali.dailyweather.data.enums.ScreenType
import mente.vali.dailyweather.domain.viewmodels.ForecastViewModel
import mente.vali.dailyweather.util.OnSwipeTouchListener

/**
 * Класс Activity, представляющий главный экран приложения.
 */
class MainActivity : AppCompatActivity() {
    // region Private Fields

    /** Поле [ForecastViewModel] для работы с данными, получаемыми от API. */
    private lateinit var forecastViewModel: ForecastViewModel
    /** Поле [NavController] для работы с навигацией. */
    private lateinit var navController: NavController

    //endregion

    // region onMethods

    /**
     * Метод, вызываемый при создании Activity.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Инициализация shared ViewModel, которая будет использоваться во всём приложении.
        forecastViewModel =
            ViewModelProviders.of(this).get(ForecastViewModel::class.java)

        setContentView(R.layout.activity_main)

        initFields()
        initObservers()
        initListeners()
        initCallbacks()
    }

    override fun onStart() {
        super.onStart()


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

    // endregion

    // region Initializators

    /** Инициализация основных полей Activity. */
    private fun initFields() {

        navController = nav_host_fragment.findNavController()
    }

    /** Инициализация всех Callback Activity. */
    private fun initCallbacks() {
        // Обработка нажатия системной кнопки "Back".
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }

        }
        onBackPressedDispatcher.addCallback(this, callback)
    }

    /** Инициализация всех Observers Activity. */
    private fun initObservers() {
        // Прослушивание для определения, когда происходит запрос данных.
        forecastViewModel.isFetching.observe(this, Observer { isFetching ->
            if (!isFetching) {
                srl_update.isRefreshing = false
            }
        })
        // Прослушивание с целью определить, инициализированы текущие данные или нет.
        forecastViewModel.isDataUnprepared.observe(this, Observer { isDataUnprepared ->
            // Если данные являются неинициализированными,
            if (isDataUnprepared) { // То обновляем UI с сокрытием,
                showProgressView()
                forecastViewModel.update()
            } else { // Иначе показываем UI.
                hideProgressView()
            }
        })
    }

    /** Инициализация всех Listeners Activity. */
    private fun initListeners() {
        // Инициализация Listener, определяющего действия в зависимости от выбранного пункта
        // BottomNavigationView.
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

        // При совершении действия Drag to update - происходит обновление.
        srl_update.setOnRefreshListener {
            forecastViewModel.update()
        }

        // Установка слушателя для обработки свайпов влево или вправо.
        srl_update.setOnTouchListener(object : OnSwipeTouchListener(this) {
            // В данном случае произойдёт переход на следующий экран.
            override fun onSwipeLeft() {
                when (forecastViewModel.currentScreenType) {
                    ScreenType.TODAY -> navigateTo(R.id.navigation_tomorrow)
                    ScreenType.TOMORROW -> navigateTo(R.id.navigation_five_days)
                    ScreenType.FIVE_DAYS -> return super.onSwipeLeft()
                }

            }
            // В данном случае произойдёт переход на предыдущий экран.
            override fun onSwipeRight() {
                when (forecastViewModel.currentScreenType) {
                    ScreenType.TODAY -> return super.onSwipeRight()
                    ScreenType.TOMORROW -> navigateTo(R.id.navigation_today)
                    ScreenType.FIVE_DAYS -> navigateTo(R.id.navigation_tomorrow)
                }
            }
        })
    }

    // endregion

    // region UI hiders/showers

    /** Отобразить показатель прогресса загрузки данных. */
    private fun showProgressView(isHiding: Boolean = true) {
        rl_progress.visibility = VISIBLE
        nav_host_fragment.view?.visibility = GONE
    }

    /** Скрыть показатель прогресса загрузки данных. */
    private fun hideProgressView() {
        rl_progress.visibility = GONE
        nav_host_fragment.view?.visibility = VISIBLE
    }

    // endregion

    // region Public Methods

    /** Получить общий объект [ForecastViewModel], инициализированный в [MainActivity]. */
    fun getSharedViewModel() = forecastViewModel

    // endregion


    // region Navigation Methods

    /** Перейти к TodayWeatherFragment в [nav_host_fragment]. */
    private fun goToToday() {
        if (navController.currentDestination?.id == R.id.tomorrowWeatherFragment) {
            navController.navigate(R.id.action_tomorrowWeatherFragment_to_todayWeatherFragment)
        } else if (forecastViewModel.currentScreenType == ScreenType.FIVE_DAYS) {
            navController.navigate(R.id.action_fiveDaysFragment_to_todayWeatherFragment)
        }
    }

    /** Перейти к TomorrowWeatherFragment в [nav_host_fragment]. */
    private fun goToTomorrow() {
        if (forecastViewModel.currentScreenType == ScreenType.TODAY) {
            navController.navigate(R.id.action_todayWeatherFragment_to_tomorrowWeatherFragment)
        } else if (navController.currentDestination?.id == R.id.fiveDaysFragment) {
            navController.navigate(R.id.action_fiveDaysFragment_to_tomorrowWeatherFragment)
        }

    }

    /** Перейти к FiveDaysWeatherFragment в [nav_host_fragment]. */
    private fun goToFiveDays() {
        if (forecastViewModel.currentScreenType == ScreenType.TODAY) {
            navController.navigate(R.id.action_todayWeatherFragment_to_fiveDaysFragment)
        } else if (forecastViewModel.currentScreenType == ScreenType.TOMORROW) {
            navController.navigate(R.id.action_tomorrowWeatherFragment_to_fiveDaysFragment)
        }
    }

    /**
     * Установить новый выбранный пункт в BottomNavigationView.
     */
    private fun navigateToToday() {
        bnv_navigation.selectedItemId = R.id.navigation_today
    }

    private fun navigateTo(navigationID: Int) {
        bnv_navigation.selectedItemId = navigationID
    }

    private fun navigateToFiveDays() {
        bnv_navigation.selectedItemId = R.id.navigation_five_days
    }

    // endregion


}
