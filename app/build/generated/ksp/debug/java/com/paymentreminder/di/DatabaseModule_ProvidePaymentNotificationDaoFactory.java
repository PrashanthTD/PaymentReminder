package com.paymentreminder.di;

import com.paymentreminder.data.local.AppDatabase;
import com.paymentreminder.data.local.PaymentNotificationDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class DatabaseModule_ProvidePaymentNotificationDaoFactory implements Factory<PaymentNotificationDao> {
  private final Provider<AppDatabase> databaseProvider;

  public DatabaseModule_ProvidePaymentNotificationDaoFactory(
      Provider<AppDatabase> databaseProvider) {
    this.databaseProvider = databaseProvider;
  }

  @Override
  public PaymentNotificationDao get() {
    return providePaymentNotificationDao(databaseProvider.get());
  }

  public static DatabaseModule_ProvidePaymentNotificationDaoFactory create(
      Provider<AppDatabase> databaseProvider) {
    return new DatabaseModule_ProvidePaymentNotificationDaoFactory(databaseProvider);
  }

  public static PaymentNotificationDao providePaymentNotificationDao(AppDatabase database) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.providePaymentNotificationDao(database));
  }
}
