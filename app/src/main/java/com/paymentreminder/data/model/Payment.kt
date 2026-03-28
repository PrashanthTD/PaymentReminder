package com.paymentreminder.data.model

import java.time.LocalDate
import java.time.temporal.ChronoUnit

enum class PaymentStatus {
    UPCOMING,
    DUE_SOON,
    OVERDUE,
    PAID;

    companion object {
        fun compute(dueDate: LocalDate, isPaid: Boolean): PaymentStatus {
            if (isPaid) return PAID
            val daysUntilDue = ChronoUnit.DAYS.between(LocalDate.now(), dueDate)
            return when {
                daysUntilDue < 0 -> OVERDUE
                daysUntilDue <= 3 -> DUE_SOON
                else -> UPCOMING
            }
        }
    }
}

data class Payment(
    val id: Long = 0,
    val vendorName: String,
    val invoiceNumber: String = "",
    val dueDate: LocalDate,
    val amount: Double = 0.0,
    val isPaid: Boolean = false,
    val paidDate: LocalDate? = null,
    val isMonthlyReminder: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
) {
    val status: PaymentStatus
        get() = PaymentStatus.compute(dueDate, isPaid)
}
