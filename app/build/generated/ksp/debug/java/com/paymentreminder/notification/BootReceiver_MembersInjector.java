package com.paymentreminder.notification;

import com.paymentreminder.data.local.PaymentDao;
import com.paymentreminder.data.local.PaymentNotificationDao;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class BootReceiver_MembersInjector implements MembersInjector<BootReceiver> {
  private final Provider<PaymentDao> paymentDaoProvider;

  private final Provider<PaymentNotificationDao> paymentNotificationDaoProvider;

  private final Provider<NotificationScheduler> notificationSchedulerProvider;

  public BootReceiver_MembersInjector(Provider<PaymentDao> paymentDaoProvider,
      Provider<PaymentNotificationDao> paymentNotificationDaoProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    this.paymentDaoProvider = paymentDaoProvider;
    this.paymentNotificationDaoProvider = paymentNotificationDaoProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  public static MembersInjector<BootReceiver> create(Provider<PaymentDao> paymentDaoProvider,
      Provider<PaymentNotificationDao> paymentNotificationDaoProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    return new BootReceiver_MembersInjector(paymentDaoProvider, paymentNotificationDaoProvider, notificationSchedulerProvider);
  }

  @Override
  public void injectMembers(BootReceiver instance) {
    injectPaymentDao(instance, paymentDaoProvider.get());
    injectPaymentNotificationDao(instance, paymentNotificationDaoProvider.get());
    injectNotificationScheduler(instance, notificationSchedulerProvider.get());
  }

  @InjectedFieldSignature("com.paymentreminder.notification.BootReceiver.paymentDao")
  public static void injectPaymentDao(BootReceiver instance, PaymentDao paymentDao) {
    instance.paymentDao = paymentDao;
  }

  @InjectedFieldSignature("com.paymentreminder.notification.BootReceiver.paymentNotificationDao")
  public static void injectPaymentNotificationDao(BootReceiver instance,
      PaymentNotificationDao paymentNotificationDao) {
    instance.paymentNotificationDao = paymentNotificationDao;
  }

  @InjectedFieldSignature("com.paymentreminder.notification.BootReceiver.notificationScheduler")
  public static void injectNotificationScheduler(BootReceiver instance,
      NotificationScheduler notificationScheduler) {
    instance.notificationScheduler = notificationScheduler;
  }
}
