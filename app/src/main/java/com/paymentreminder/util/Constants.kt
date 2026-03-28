package com.paymentreminder.util

object Constants {
    const val DATABASE_NAME = "payment_reminder_db"
    const val DRIVE_BACKUP_FILENAME = "payment_backup.enc"
    const val DRIVE_BACKUP_MIME_TYPE = "application/octet-stream"
    const val NOTIFICATION_CHANNEL_ID = "payment_reminders"
    const val DUE_SOON_DAYS = 3
    const val WORKER_TAG = "payment_reminder_worker"
    const val WORKER_REPEAT_INTERVAL_HOURS = 12L
    const val CRYPTO_ALGORITHM = "AES/GCM/NoPadding"
    const val CRYPTO_KEY_SIZE = 256
    const val CRYPTO_IV_SIZE = 12
    const val PBKDF2_ITERATIONS = 100_000
    const val PBKDF2_KEY_LENGTH = 256
    const val APP_SALT = "PaymentReminderV1Salt"

    /** Replace with your Web Client ID from Google Cloud Console */
    const val WEB_CLIENT_ID = "YOUR_WEB_CLIENT_ID.apps.googleusercontent.com"
}
