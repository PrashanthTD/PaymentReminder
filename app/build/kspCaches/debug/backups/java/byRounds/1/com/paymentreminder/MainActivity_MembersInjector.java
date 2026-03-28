package com.paymentreminder;

import com.paymentreminder.ui.theme.ThemePreferenceManager;
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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<ThemePreferenceManager> themePreferenceManagerProvider;

  public MainActivity_MembersInjector(
      Provider<ThemePreferenceManager> themePreferenceManagerProvider) {
    this.themePreferenceManagerProvider = themePreferenceManagerProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<ThemePreferenceManager> themePreferenceManagerProvider) {
    return new MainActivity_MembersInjector(themePreferenceManagerProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectThemePreferenceManager(instance, themePreferenceManagerProvider.get());
  }

  @InjectedFieldSignature("com.paymentreminder.MainActivity.themePreferenceManager")
  public static void injectThemePreferenceManager(MainActivity instance,
      ThemePreferenceManager themePreferenceManager) {
    instance.themePreferenceManager = themePreferenceManager;
  }
}
