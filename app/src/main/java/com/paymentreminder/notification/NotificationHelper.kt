package com.paymentreminder.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.paymentreminder.MainActivity
import com.paymentreminder.R
import com.paymentreminder.data.model.Payment
import com.paymentreminder.data.model.PaymentStatus
import com.paymentreminder.util.Constants
import com.paymentreminder.util.DateUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun createChannel() {
        val channel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = context.getString(R.string.notification_channel_desc)
        }
        context.getSystemService(NotificationManager::class.java)
            .createNotificationChannel(channel)
    }

    fun showReminder(payment: Payment, notificationId: Int) {
        val title = when (payment.status) {
            PaymentStatus.OVERDUE -> "Payment Overdue!"
            PaymentStatus.DUE_SOON -> "Payment Due Soon"
            else -> "Payment Reminder"
        }
        val body = "${payment.vendorName} — ${DateUtils.formatCurrency(payment.amount)} due ${DateUtils.formatForDisplay(payment.dueDate)}"
        notify(notificationId, title, body)
    }

    fun showPaymentAdded(payment: Payment) {
        val body = "${payment.vendorName} — ${DateUtils.formatCurrency(payment.amount)} due ${DateUtils.formatForDisplay(payment.dueDate)}"
        notify((payment.id + 500_000L).toInt(), "Payment Added", body)
    }

    fun showScheduledReminder(notifId: Int, vendorName: String, amount: Double, dueDateDisplay: String) {
        val body = "$vendorName — ${DateUtils.formatCurrency(amount)} due $dueDateDisplay"
        notify(notifId, "Payment Reminder", body)
    }

    private fun notify(notifId: Int, title: String, body: String) {
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context, notifId, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, Constants.NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NotificationManagerCompat.from(context).notify(notifId, notification)
        }
    }
}
