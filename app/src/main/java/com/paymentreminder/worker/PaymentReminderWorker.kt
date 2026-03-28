package com.paymentreminder.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.paymentreminder.data.local.PaymentDao
import com.paymentreminder.data.local.toDomain
import com.paymentreminder.notification.NotificationHelper
import com.paymentreminder.util.Constants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate

@HiltWorker
class PaymentReminderWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val paymentDao: PaymentDao,
    private val notificationHelper: NotificationHelper
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        val today = LocalDate.now()
        val todayEpoch = today.toEpochDay()
        val dueSoonEndEpoch = today.plusDays(Constants.DUE_SOON_DAYS.toLong()).toEpochDay()

        val overduePayments = paymentDao.getOverduePayments(todayEpoch)
        val dueSoonPayments = paymentDao.getDueSoonPayments(todayEpoch, dueSoonEndEpoch)

        var notifId = (System.currentTimeMillis() % Int.MAX_VALUE).toInt()

        overduePayments.forEach { entity ->
            notificationHelper.showReminder(entity.toDomain(), notifId++)
        }

        dueSoonPayments.forEach { entity ->
            notificationHelper.showReminder(entity.toDomain(), notifId++)
        }

        return Result.success()
    }
}
