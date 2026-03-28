package com.paymentreminder.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface PaymentNotificationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(notifications: List<PaymentNotificationEntity>): List<Long>

    @Query("SELECT * FROM payment_notifications WHERE paymentId = :paymentId")
    suspend fun getForPayment(paymentId: Long): List<PaymentNotificationEntity>

    @Query("SELECT * FROM payment_notifications WHERE paymentId = :paymentId AND isCustom = 1")
    suspend fun getCustomForPayment(paymentId: Long): List<PaymentNotificationEntity>

    @Query("DELETE FROM payment_notifications WHERE paymentId = :paymentId")
    suspend fun deleteForPayment(paymentId: Long)

    @Query("SELECT * FROM payment_notifications WHERE scheduledAt > :nowMillis")
    suspend fun getFutureNotifications(nowMillis: Long): List<PaymentNotificationEntity>
}
