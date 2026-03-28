package com.paymentreminder.ui.screens.addpayment;

import com.paymentreminder.data.repository.PaymentRepository;
import com.paymentreminder.notification.NotificationHelper;
import com.paymentreminder.notification.NotificationScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class AddPaymentViewModel_Factory implements Factory<AddPaymentViewModel> {
  private final Provider<PaymentRepository> repositoryProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private final Provider<NotificationScheduler> notificationSchedulerProvider;

  public AddPaymentViewModel_Factory(Provider<PaymentRepository> repositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.notificationHelperProvider = notificationHelperProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  @Override
  public AddPaymentViewModel get() {
    return newInstance(repositoryProvider.get(), notificationHelperProvider.get(), notificationSchedulerProvider.get());
  }

  public static AddPaymentViewModel_Factory create(Provider<PaymentRepository> repositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    return new AddPaymentViewModel_Factory(repositoryProvider, notificationHelperProvider, notificationSchedulerProvider);
  }

  public static AddPaymentViewModel newInstance(PaymentRepository repository,
      NotificationHelper notificationHelper, NotificationScheduler notificationScheduler) {
    return new AddPaymentViewModel(repository, notificationHelper, notificationScheduler);
  }
}
