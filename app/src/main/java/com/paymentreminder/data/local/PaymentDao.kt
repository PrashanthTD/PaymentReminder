package com.paymentreminder.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PaymentDao {

    @Query("SELECT * FROM payments ORDER BY dueDateEpoch ASC")
    fun getAllPayments(): Flow<List<PaymentEntity>>

    @Query("SELECT * FROM payments WHERE isPaid = 0 AND dueDateEpoch < :todayEpoch")
    suspend fun getOverduePayments(todayEpoch: Long): List<PaymentEntity>

    @Query("SELECT * FROM payments WHERE isPaid = 0 AND dueDateEpoch BETWEEN :todayEpoch AND :endEpoch")
    suspend fun getDueSoonPayments(todayEpoch: Long, endEpoch: Long): List<PaymentEntity>

    @Query("SELECT * FROM payments WHERE id = :id")
    suspend fun getPaymentById(id: Long): PaymentEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPayment(payment: PaymentEntity): Long

    @Update
    suspend fun updatePayment(payment: PaymentEntity)

    @Delete
    suspend fun deletePayment(payment: PaymentEntity)

    @Query("UPDATE payments SET isPaid = 1, paidDateEpoch = :paidEpoch, updatedAt = :now WHERE id = :id")
    suspend fun markAsPaid(id: Long, paidEpoch: Long, now: Long)

    @Query("SELECT COUNT(*) FROM payments")
    fun getTotalCount(): Flow<Int>

    @Query("SELECT COUNT(*) FROM payments WHERE isPaid = 0 AND dueDateEpoch < :todayEpoch")
    fun getOverdueCount(todayEpoch: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM payments WHERE isPaid = 0 AND dueDateEpoch BETWEEN :todayEpoch AND :endEpoch")
    fun getDueSoonCount(todayEpoch: Long, endEpoch: Long): Flow<Int>

    @Query("SELECT COUNT(*) FROM payments WHERE isPaid = 1")
    fun getPaidCount(): Flow<Int>

    @Query("SELECT COALESCE(SUM(amount), 0.0) FROM payments WHERE isPaid = 0")
    fun getTotalUnpaidAmount(): Flow<Double>

    @Query("SELECT * FROM payments WHERE vendorName = :vendorName AND dueDateEpoch = :dueDateEpoch LIMIT 1")
    suspend fun findByVendorAndDate(vendorName: String, dueDateEpoch: Long): PaymentEntity?
}
