package com.paymentreminder.ui.screens.settings

import androidx.lifecycle.ViewModel
import com.paymentreminder.ui.theme.ThemeMode
import com.paymentreminder.ui.theme.ThemePreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreferenceManager: ThemePreferenceManager
) : ViewModel() {

    val themeMode = themePreferenceManager.themeMode

    fun setThemeMode(mode: ThemeMode) {
        themePreferenceManager.setThemeMode(mode)
    }
}
