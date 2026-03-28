package com.paymentreminder.ui.screens.editpayment;

import androidx.lifecycle.SavedStateHandle;
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
public final class EditPaymentViewModel_Factory implements Factory<EditPaymentViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<PaymentRepository> repositoryProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private final Provider<NotificationScheduler> notificationSchedulerProvider;

  public EditPaymentViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<PaymentRepository> repositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.repositoryProvider = repositoryProvider;
    this.notificationHelperProvider = notificationHelperProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  @Override
  public EditPaymentViewModel get() {
    return newInstance(savedStateHandleProvider.get(), repositoryProvider.get(), notificationHelperProvider.get(), notificationSchedulerProvider.get());
  }

  public static EditPaymentViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<PaymentRepository> repositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    return new EditPaymentViewModel_Factory(savedStateHandleProvider, repositoryProvider, notificationHelperProvider, notificationSchedulerProvider);
  }

  public static EditPaymentViewModel newInstance(SavedStateHandle savedStateHandle,
      PaymentRepository repository, NotificationHelper notificationHelper,
      NotificationScheduler notificationScheduler) {
    return new EditPaymentViewModel(savedStateHandle, repository, notificationHelper, notificationScheduler);
  }
}
