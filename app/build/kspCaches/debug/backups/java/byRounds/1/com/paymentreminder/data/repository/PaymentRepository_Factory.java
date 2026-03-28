package com.paymentreminder.data.repository;

import com.paymentreminder.data.local.PaymentDao;
import com.paymentreminder.data.local.PaymentNotificationDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
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
public final class PaymentRepository_Factory implements Factory<PaymentRepository> {
  private final Provider<PaymentDao> daoProvider;

  private final Provider<PaymentNotificationDao> notificationDaoProvider;

  public PaymentRepository_Factory(Provider<PaymentDao> daoProvider,
      Provider<PaymentNotificationDao> notificationDaoProvider) {
    this.daoProvider = daoProvider;
    this.notificationDaoProvider = notificationDaoProvider;
  }

  @Override
  public PaymentRepository get() {
    return newInstance(daoProvider.get(), notificationDaoProvider.get());
  }

  public static PaymentRepository_Factory create(Provider<PaymentDao> daoProvider,
      Provider<PaymentNotificationDao> notificationDaoProvider) {
    return new PaymentRepository_Factory(daoProvider, notificationDaoProvider);
  }

  public static PaymentRepository newInstance(PaymentDao dao,
      PaymentNotificationDao notificationDao) {
    return new PaymentRepository(dao, notificationDao);
  }
}
