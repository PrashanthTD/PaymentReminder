package com.paymentreminder.ui.screens.payments;

import com.paymentreminder.data.repository.PaymentRepository;
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

  public PaymentListViewModel_Factory(Provider<PaymentRepository> repositoryProvider) {
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public PaymentListViewModel get() {
    return newInstance(repositoryProvider.get());
  }

  public static PaymentListViewModel_Factory create(
      Provider<PaymentRepository> repositoryProvider) {
    return new PaymentListViewModel_Factory(repositoryProvider);
  }

  public static PaymentListViewModel newInstance(PaymentRepository repository) {
    return new PaymentListViewModel(repository);
  }
}
