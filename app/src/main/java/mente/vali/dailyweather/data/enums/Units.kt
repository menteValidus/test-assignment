package mente.vali.dailyweather.data.enums

/** Единицы градуса. */
enum class Units {
    CELSIUS {
        override fun getString() = "C"

        override fun getUnitString() = "metric"

    },
    FAHRENHEIT {
        override fun getString() = "F"

        override fun getUnitString() = "imperial"
    };

    /**
     * Получить строку со значениями единицы градуса.
     * Для отображения на экране.
     */
    abstract fun getString(): String

    /**
     * Получить строку со значениями единицы градуса.
     * Для передачи на сервер API.
     */
    abstract fun getUnitString(): String

    companion object {
        /**
         * Статический метод для конвертации строки в [Units].
         * Стандартное значение [Units.CELSIUS].
         */
        fun get(conditionString: String): Units {
            return when (conditionString) {
                CELSIUS.getUnitString() -> CELSIUS
                FAHRENHEIT.getUnitString() -> FAHRENHEIT
                else -> CELSIUS
            }
        }
    }
}