package com.paymentreminder.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.ChronoUnit

object DateUtils {

    private val displayFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

    fun formatForDisplay(date: LocalDate): String = date.format(displayFormatter)

    fun toEpochDay(date: LocalDate): Long = date.toEpochDay()

    fun fromEpochDay(epochDay: Long): LocalDate = LocalDate.ofEpochDay(epochDay)

    fun daysUntil(date: LocalDate): Long =
        ChronoUnit.DAYS.between(LocalDate.now(), date)

    fun todayEpoch(): Long = LocalDate.now().toEpochDay()

    fun dueSoonEndEpoch(): Long = LocalDate.now().plusDays(Constants.DUE_SOON_DAYS.toLong()).toEpochDay()

    fun formatCurrency(amount: Double): String = String.format("₹%.2f", amount)
}
