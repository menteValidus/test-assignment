package mente.vali.dailyweather.util

import android.content.Context
import android.widget.Toast

/**
 * Вывод сообщений посредством toast-оповещений.
 */
fun displayMessage(context: Context, message: String, isLong: Boolean = true) {
    if (isLong) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    } else {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}