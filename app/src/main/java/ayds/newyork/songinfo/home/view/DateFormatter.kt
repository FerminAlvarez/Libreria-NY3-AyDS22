package ayds.newyork.songinfo.home.view

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


        private fun getYearFormat(year: String): String {
            var format = "N/A"
            if (year.isNotEmpty()) {
                format =
                    year + if (isLeapYear(year.toInt())) " (leap year)" else " (not a leap year)"
            }
            return format
        }


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
            val monthFormat = SimpleDateFormat("yyyy-MM", Locale.US)
            val newMonthFormat = "MMMM, yyyy"

            return applyFormat(monthFormat, newMonthFormat, date)
        }


        private fun getDayFormat(date: String): String {
            val dayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
            val newDayFormat = "dd/MM/yyyy"

            return applyFormat(dayFormat, newDayFormat, date)
        }

        private fun applyFormat(
            dateFormat: SimpleDateFormat,
            newDateFormat: String,
            date: String
        ): String {
            var format = "N/A"
            val releaseDate = dateFormat.parse(date)

            if (releaseDate != null) {
                dateFormat.applyPattern(newDateFormat)
                format = dateFormat.format(releaseDate)
            }

            return format
        }
    }
}