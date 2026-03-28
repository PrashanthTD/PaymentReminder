package com.paymentreminder.util

object Constants {
    const val DATABASE_NAME = "payment_reminder_db"
    const val NOTIFICATION_CHANNEL_ID = "payment_reminders"
    const val DUE_SOON_DAYS = 3
    const val WORKER_TAG = "payment_reminder_worker"
    const val WORKER_REPEAT_INTERVAL_HOURS = 12L
}
