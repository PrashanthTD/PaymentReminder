package com.paymentreminder.ui.screens.payments;

import com.paymentreminder.data.repository.PaymentRepository;
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
public final class PaymentListViewModel_Factory implements Factory<PaymentListViewModel> {
  private final Provider<PaymentRepository> repositoryProvider;

  private final Provider<NotificationScheduler> notificationSchedulerProvider;

  public PaymentListViewModel_Factory(Provider<PaymentRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    this.repositoryProvider = repositoryProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  @Override
  public PaymentListViewModel get() {
    return newInstance(repositoryProvider.get(), notificationSchedulerProvider.get());
  }

  public static PaymentListViewModel_Factory create(Provider<PaymentRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    return new PaymentListViewModel_Factory(repositoryProvider, notificationSchedulerProvider);
  }

  public static PaymentListViewModel newInstance(PaymentRepository repository,
      NotificationScheduler notificationScheduler) {
    return new PaymentListViewModel(repository, notificationScheduler);
  }
}
