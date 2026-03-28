package com.paymentreminder;

import androidx.hilt.work.HiltWorkerFactory;
import com.paymentreminder.notification.NotificationHelper;
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
public final class PaymentReminderApp_MembersInjector implements MembersInjector<PaymentReminderApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  public PaymentReminderApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public static MembersInjector<PaymentReminderApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new PaymentReminderApp_MembersInjector(workerFactoryProvider, notificationHelperProvider);
  }

  @Override
  public void injectMembers(PaymentReminderApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  @InjectedFieldSignature("com.paymentreminder.PaymentReminderApp.workerFactory")
  public static void injectWorkerFactory(PaymentReminderApp instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }

  @InjectedFieldSignature("com.paymentreminder.PaymentReminderApp.notificationHelper")
  public static void injectNotificationHelper(PaymentReminderApp instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
