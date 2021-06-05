package it.gasadvisor.gas_backend.util

import java.text.SimpleDateFormat
import java.time.Instant

class TimeUtils {
    companion object {
        const val PRICE_CSV_DATE_FORMAT = "dd/MM/yyyy HH:mm:ss"
        fun stringToInstant(formatter: SimpleDateFormat, date: String): Instant? {
            return try {
                formatter.parse(date)
            } catch (e: Exception) {
                null
            }?.toInstant()
        }

    }
}
