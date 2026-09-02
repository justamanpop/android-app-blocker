package com.example.appblocker.ui.shared

import com.example.appblocker.ui.screens.settingsScreen.HoursMinutesDaysFieldValues

fun formatSeconds(seconds: Long): String {
    val hoursMinutesDays = HoursMinutesDaysFieldValues.fromSeconds(seconds)
    val formattedString = buildString {
        if (hoursMinutesDays.days.toInt() != 0) {
            append("${hoursMinutesDays.days} days")
        }
        if (hoursMinutesDays.hours.toInt() != 0) {
            append(" ${hoursMinutesDays.hours} hours")
        }
        if (hoursMinutesDays.minutes.toInt() != 0) {
            append(" ${hoursMinutesDays.minutes} minutes")
        }
    }
    return formattedString.trim()
}
