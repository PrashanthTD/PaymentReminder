package com.paymentreminder.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.paymentreminder.data.model.Payment
import java.time.LocalDate

@Entity(tableName = "payments")
data class PaymentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val vendorName: String,
    val invoiceNumber: String = "",
    val dueDateEpoch: Long,
    val amount: Double = 0.0,
    val isPaid: Boolean = false,
    val paidDateEpoch: Long? = null,
    val isMonthlyReminder: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

fun PaymentEntity.toDomain(): Payment = Payment(
    id = id,
    vendorName = vendorName,
    invoiceNumber = invoiceNumber,
    dueDate = LocalDate.ofEpochDay(dueDateEpoch),
    amount = amount,
    isPaid = isPaid,
    paidDate = paidDateEpoch?.let { LocalDate.ofEpochDay(it) },
    isMonthlyReminder = isMonthlyReminder,
    createdAt = createdAt,
    updatedAt = updatedAt
)

fun Payment.toEntity(): PaymentEntity = PaymentEntity(
    id = id,
    vendorName = vendorName,
    invoiceNumber = invoiceNumber,
    dueDateEpoch = dueDate.toEpochDay(),
    amount = amount,
    isPaid = isPaid,
    paidDateEpoch = paidDate?.toEpochDay(),
    isMonthlyReminder = isMonthlyReminder,
    createdAt = createdAt,
    updatedAt = updatedAt
)
