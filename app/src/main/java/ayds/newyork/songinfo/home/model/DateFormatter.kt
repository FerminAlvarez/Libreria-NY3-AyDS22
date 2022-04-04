package ayds.newyork.songinfo.home.model

import java.text.SimpleDateFormat
import java.util.*

class DateFormatter {

    companion object {
        fun getReleaseDate(date: String, precisionDate: String) =
            when (precisionDate) {
                "day" -> getDayFormat(date)
                "month" -> getMonthFormat(date)
                "year" -> getYearFormat(date)
                else -> ""
            }


        private fun getYearFormat(year: String): String =
            year + if (isLeapYear(year.toInt())) " (leap year)" else " (not a leap year)"


        private fun isLeapYear(year: Int): Boolean {
            return when {
                year % 4 != 0 -> {
                    false
                }
                year % 400 == 0 -> {
                    true
                }
                else -> year % 100 != 0
            }
        }

        private fun getMonthFormat(date: String): String {
            val dayFormat = SimpleDateFormat("yyyy-MM", Locale.US)
            val newDayFormat = "MMMM, yyyy"
            val releaseDate = dayFormat.parse(date)
            dayFormat.applyPattern(newDayFormat)
            return dayFormat.format(releaseDate!!)
        }


        private fun getDayFormat(date: String): String {
            val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val newDayFormat = "dd/MM/yyyy"
            val releaseDate = dayFormat.parse(date)
            dayFormat.applyPattern(newDayFormat)
            return dayFormat.format(releaseDate!!)
        }
    }
}