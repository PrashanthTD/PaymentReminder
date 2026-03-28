package com.paymentreminder.data.export

data class ExportPayment(
    val vendorName: String,
    val invoiceNumber: String,
    val dueDateEpoch: Long,
    val amount: Double,
    val isPaid: Boolean,
    val paidDateEpoch: Long?,
    val isMonthlyReminder: Boolean,
    val customNotifications: List<Long>,
    val createdAt: Long = System.currentTimeMillis()
)
