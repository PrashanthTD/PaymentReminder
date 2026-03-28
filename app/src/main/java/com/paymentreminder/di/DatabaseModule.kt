package com.paymentreminder.di

import android.content.Context
import androidx.room.Room
import com.paymentreminder.data.local.AppDatabase
import com.paymentreminder.data.local.PaymentDao
import com.paymentreminder.data.local.PaymentNotificationDao
import com.paymentreminder.util.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, Constants.DATABASE_NAME)
            .addMigrations(AppDatabase.MIGRATION_4_5)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun providePaymentDao(database: AppDatabase): PaymentDao =
        database.paymentDao()

    @Provides
    @Singleton
    fun providePaymentNotificationDao(database: AppDatabase): PaymentNotificationDao =
        database.paymentNotificationDao()
}
