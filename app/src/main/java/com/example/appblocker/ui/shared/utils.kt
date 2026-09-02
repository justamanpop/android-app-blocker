package com.example.appblocker.ui.shared

import com.example.appblocker.ui.screens.settingsScreen.HoursMinutesDaysFieldValues

fun formatSeconds(seconds: Long): String {
    val hoursMinutesDays = HoursMinutesDaysFieldValues.fromSeconds(seconds)
    val formattedString = buildString {
        val days = hoursMinutesDays.days.toInt()
        if (days != 0) {
            append("${hoursMinutesDays.days} ${if (days != 1) "days" else "day"}")
        }

        val hours = hoursMinutesDays.hours.toInt()
        if (hours != 0) {
            append(" ${hoursMinutesDays.hours} ${if (hours != 1) "hours" else "hour"}")
        }

        val minutes = hoursMinutesDays.minutes.toInt()
        if (minutes != 0) {
            append(" ${hoursMinutesDays.minutes} ${if (minutes != 1) "minutes" else "minute"}")
        }
    }
    if (formattedString == "") {
        return "less than a minute"
    }
    return formattedString.trim()
}
