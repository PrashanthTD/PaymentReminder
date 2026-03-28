package com.paymentreminder.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "payment_notifications",
    foreignKeys = [
        ForeignKey(
            entity = PaymentEntity::class,
            parentColumns = ["id"],
            childColumns = ["paymentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("paymentId")]
)
data class PaymentNotificationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val paymentId: Long,
    val scheduledAt: Long,
    @ColumnInfo(defaultValue = "0")
    val isCustom: Boolean = false
)
