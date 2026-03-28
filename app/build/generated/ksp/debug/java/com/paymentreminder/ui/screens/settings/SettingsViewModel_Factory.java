package com.paymentreminder.ui.screens.settings;

import com.paymentreminder.data.repository.PaymentRepository;
import com.paymentreminder.notification.NotificationScheduler;
import com.paymentreminder.ui.theme.ThemePreferenceManager;
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<ThemePreferenceManager> themePreferenceManagerProvider;

  private final Provider<PaymentRepository> repositoryProvider;

  private final Provider<NotificationScheduler> notificationSchedulerProvider;

  public SettingsViewModel_Factory(Provider<ThemePreferenceManager> themePreferenceManagerProvider,
      Provider<PaymentRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    this.themePreferenceManagerProvider = themePreferenceManagerProvider;
    this.repositoryProvider = repositoryProvider;
    this.notificationSchedulerProvider = notificationSchedulerProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(themePreferenceManagerProvider.get(), repositoryProvider.get(), notificationSchedulerProvider.get());
  }

  public static SettingsViewModel_Factory create(
      Provider<ThemePreferenceManager> themePreferenceManagerProvider,
      Provider<PaymentRepository> repositoryProvider,
      Provider<NotificationScheduler> notificationSchedulerProvider) {
    return new SettingsViewModel_Factory(themePreferenceManagerProvider, repositoryProvider, notificationSchedulerProvider);
  }

  public static SettingsViewModel newInstance(ThemePreferenceManager themePreferenceManager,
      PaymentRepository repository, NotificationScheduler notificationScheduler) {
    return new SettingsViewModel(themePreferenceManager, repository, notificationScheduler);
  }
}
