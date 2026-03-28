package com.paymentreminder.notification

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class NotificationReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context, intent: Intent) {
        val notifId = intent.getLongExtra(EXTRA_PAYMENT_ID, -1L).toInt()
        val vendorName = intent.getStringExtra(EXTRA_VENDOR_NAME) ?: return
        val amount = intent.getDoubleExtra(EXTRA_AMOUNT, 0.0)
        val dueDateDisplay = intent.getStringExtra(EXTRA_DUE_DATE_DISPLAY) ?: return
        notificationHelper.showScheduledReminder(notifId, vendorName, amount, dueDateDisplay)
    }

    companion object {
        const val EXTRA_PAYMENT_ID = "extra_payment_id"
        const val EXTRA_VENDOR_NAME = "extra_vendor_name"
        const val EXTRA_AMOUNT = "extra_amount"
        const val EXTRA_DUE_DATE_DISPLAY = "extra_due_date_display"
    }
}
