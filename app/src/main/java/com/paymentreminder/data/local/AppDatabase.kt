package com.paymentreminder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [PaymentEntity::class, PaymentNotificationEntity::class],
    version = 4,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun paymentDao(): PaymentDao
    abstract fun paymentNotificationDao(): PaymentNotificationDao
}
