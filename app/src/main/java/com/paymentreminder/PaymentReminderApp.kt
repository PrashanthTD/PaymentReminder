package com.paymentreminder

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.paymentreminder.notification.NotificationHelper
import com.paymentreminder.util.Constants
import com.paymentreminder.worker.PaymentReminderWorker
import dagger.hilt.android.HiltAndroidApp
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class PaymentReminderApp : Application(), Configuration.Provider {

    @Inject lateinit var workerFactory: HiltWorkerFactory
    @Inject lateinit var notificationHelper: NotificationHelper

    override fun onCreate() {
        super.onCreate()
        notificationHelper.createChannel()
        scheduleReminders()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    private fun scheduleReminders() {
        val request = PeriodicWorkRequestBuilder<PaymentReminderWorker>(
            Constants.WORKER_REPEAT_INTERVAL_HOURS, TimeUnit.HOURS
        ).setConstraints(Constraints.Builder().build())
            .addTag(Constants.WORKER_TAG)
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            Constants.WORKER_TAG,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }
}
