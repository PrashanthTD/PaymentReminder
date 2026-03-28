package com.paymentreminder.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.paymentreminder.data.local.PaymentDao;
import com.paymentreminder.notification.NotificationHelper;
import dagger.internal.DaggerGenerated;
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
public final class PaymentReminderWorker_Factory {
  private final Provider<PaymentDao> paymentDaoProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  public PaymentReminderWorker_Factory(Provider<PaymentDao> paymentDaoProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.paymentDaoProvider = paymentDaoProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public PaymentReminderWorker get(Context context, WorkerParameters workerParams) {
    return newInstance(context, workerParams, paymentDaoProvider.get(), notificationHelperProvider.get());
  }

  public static PaymentReminderWorker_Factory create(Provider<PaymentDao> paymentDaoProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new PaymentReminderWorker_Factory(paymentDaoProvider, notificationHelperProvider);
  }

  public static PaymentReminderWorker newInstance(Context context, WorkerParameters workerParams,
      PaymentDao paymentDao, NotificationHelper notificationHelper) {
    return new PaymentReminderWorker(context, workerParams, paymentDao, notificationHelper);
  }
}
