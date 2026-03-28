package com.paymentreminder.notification;

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
public final class NotificationReceiver_MembersInjector implements MembersInjector<NotificationReceiver> {
  private final Provider<NotificationHelper> notificationHelperProvider;

  public NotificationReceiver_MembersInjector(
      Provider<NotificationHelper> notificationHelperProvider) {
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public static MembersInjector<NotificationReceiver> create(
      Provider<NotificationHelper> notificationHelperProvider) {
    return new NotificationReceiver_MembersInjector(notificationHelperProvider);
  }

  @Override
  public void injectMembers(NotificationReceiver instance) {
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  @InjectedFieldSignature("com.paymentreminder.notification.NotificationReceiver.notificationHelper")
  public static void injectNotificationHelper(NotificationReceiver instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
