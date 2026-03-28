package com.paymentreminder.data.repository

import com.paymentreminder.data.local.PaymentDao
import com.paymentreminder.data.local.PaymentNotificationDao
import com.paymentreminder.data.local.PaymentNotificationEntity
import com.paymentreminder.data.local.toDomain
import com.paymentreminder.data.local.toEntity
import com.paymentreminder.data.model.Payment
import com.paymentreminder.util.DateUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PaymentRepository @Inject constructor(
    private val dao: PaymentDao,
    private val notificationDao: PaymentNotificationDao
) {

    fun getAllPayments(): Flow<List<Payment>> =
        dao.getAllPayments().map { list -> list.map { it.toDomain() } }

    fun getUnpaidPayments(): Flow<List<Payment>> =
        dao.getUnpaidPayments().map { list -> list.map { it.toDomain() } }

    fun getTotalCount(): Flow<Int> = dao.getTotalCount()

    fun getOverdueCount(): Flow<Int> =
        dao.getOverdueCount(DateUtils.todayEpoch())

    fun getDueSoonCount(): Flow<Int> =
        dao.getDueSoonCount(DateUtils.todayEpoch(), DateUtils.dueSoonEndEpoch())

    fun getPaidCount(): Flow<Int> = dao.getPaidCount()

    fun getTotalUnpaidAmount(): Flow<Double> = dao.getTotalUnpaidAmount()

    suspend fun addPayment(payment: Payment): Long =
        dao.insertPayment(payment.toEntity())

    suspend fun updatePayment(payment: Payment) =
        dao.updatePayment(payment.toEntity().copy(updatedAt = System.currentTimeMillis()))

    suspend fun deletePayment(payment: Payment) =
        dao.deletePayment(payment.toEntity())

    suspend fun markAsPaid(paymentId: Long) =
        dao.markAsPaid(paymentId, DateUtils.todayEpoch(), System.currentTimeMillis())

    suspend fun getPaymentById(id: Long): Payment? =
        dao.getPaymentById(id)?.toDomain()

    suspend fun getAllUnpaidSync(): List<Payment> =
        dao.getAllUnpaidSync().map { it.toDomain() }

    /** Insert notification schedules for a payment. Returns the generated IDs. */
    suspend fun addNotifications(paymentId: Long, scheduledAtList: List<Long>): List<Long> {
        val entities = scheduledAtList.map {
            PaymentNotificationEntity(paymentId = paymentId, scheduledAt = it)
        }
        return notificationDao.insertAll(entities)
    }

    suspend fun getNotificationsForPayment(paymentId: Long): List<PaymentNotificationEntity> =
        notificationDao.getForPayment(paymentId)

    suspend fun deleteNotificationsForPayment(paymentId: Long) =
        notificationDao.deleteForPayment(paymentId)
}
