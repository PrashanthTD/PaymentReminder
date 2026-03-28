package com.paymentreminder.ui.screens.addpayment

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.UUID

data class NotificationDraft(
    val localId: String = UUID.randomUUID().toString(),
    val dateTime: LocalDateTime
) {
    val displayText: String
        get() {
            val datePart = DateTimeFormatter.ofPattern("MMM d, yyyy").format(dateTime)
            val h = dateTime.hour
            val m = dateTime.minute
            val amPm = if (h < 12) "AM" else "PM"
            val dispH = when { h == 0 -> 12; h > 12 -> h - 12; else -> h }
            return "$datePart at ${String.format("%d:%02d %s", dispH, m, amPm)}"
        }

    val epochMillis: Long
        get() = dateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
}
