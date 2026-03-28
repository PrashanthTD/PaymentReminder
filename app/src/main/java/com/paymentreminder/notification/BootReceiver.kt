package com.paymentreminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.paymentreminder.data.local.PaymentDao
import com.paymentreminder.data.local.PaymentNotificationDao
import com.paymentreminder.util.Constants
import com.paymentreminder.worker.PaymentReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class BootReceiver : BroadcastReceiver() {

    @Inject lateinit var paymentDao: PaymentDao
    @Inject lateinit var paymentNotificationDao: PaymentNotificationDao
    @Inject lateinit var notificationScheduler: NotificationScheduler

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED &&
            intent.action != "android.intent.action.QUICKBOOT_POWERON"
        ) return

        // Re-enqueue the periodic WorkManager task
        val request = PeriodicWorkRequestBuilder<PaymentReminderWorker>(
            Constants.WORKER_REPEAT_INTERVAL_HOURS, TimeUnit.HOURS
        ).setConstraints(Constraints.Builder().build())
            .addTag(Constants.WORKER_TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            Constants.WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )

        // Re-schedule all future individual alarms from the database
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val now = System.currentTimeMillis()
                val futureNotifs = paymentNotificationDao.getFutureNotifications(now)
                futureNotifs.forEach { notif ->
                    val payment = paymentDao.getPaymentById(notif.paymentId) ?: return@forEach
                    notificationScheduler.schedule(
                        notifId = notif.id.toInt(),
                        vendorName = payment.vendorName,
                        amount = payment.amount,
                        dueDate = LocalDate.ofEpochDay(payment.dueDateEpoch),
                        triggerMillis = notif.scheduledAt
                    )
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
