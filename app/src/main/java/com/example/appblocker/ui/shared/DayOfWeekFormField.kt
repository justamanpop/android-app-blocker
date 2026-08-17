package com.example.appblocker.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appblocker.ui.theme.Surface
import com.example.appblocker.ui.theme.TextPrimary
import com.example.appblocker.ui.theme.TextSecondary
import kotlinx.datetime.DayOfWeek

@Composable
fun DaysOfWeekSelect(
    daysState: Map<DayOfWeek, Boolean>,
    readonly: Boolean,
    modifier: Modifier = Modifier,
    onDayClick: (daysState: Map<DayOfWeek, Boolean>) -> Unit = {},
    dayBoxSize: Pair<Dp, Dp> = Pair(40.dp, 40.dp),
    dayTextSize: TextUnit = 24.sp,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxWidth()
    ) {
        DayOfWeek.entries.forEach { d ->
            Box(
                Modifier
                    .clip(CircleShape)
                    .background(Surface)
                    .height(dayBoxSize.first)
                    .width(dayBoxSize.second)
                    .padding(4.dp)
                    .clickable(onClick = {
                        if (!readonly) {
                            onDayClick(daysState + (d to !daysState[d]!!))
                        }
                    })
            ) {
                Text(
                    d.toString().substring(0, 1),
                    color = if (daysState[d] == true) TextPrimary else TextSecondary,
                    fontSize = dayTextSize,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}