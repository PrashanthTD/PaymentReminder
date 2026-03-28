package com.paymentreminder.ui.screens.settings

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentreminder.data.export.ExportImportManager
import com.paymentreminder.data.export.ExportPayment
import com.paymentreminder.data.model.Payment
import com.paymentreminder.data.repository.PaymentRepository
import com.paymentreminder.notification.NotificationScheduler
import com.paymentreminder.ui.theme.ThemeMode
import com.paymentreminder.ui.theme.ThemePreferenceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import javax.inject.Inject

data class ImportState(
    val showConflictDialog: Boolean = false,
    val totalCount: Int = 0,
    val newCount: Int = 0,
    val conflictCount: Int = 0,
    val importSuccess: Boolean = false,
    val importedCount: Int = 0,
    val error: String? = null
)

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val themePreferenceManager: ThemePreferenceManager,
    private val repository: PaymentRepository,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    val themeMode = themePreferenceManager.themeMode

    private val _importState = MutableStateFlow(ImportState())
    val importState: StateFlow<ImportState> = _importState.asStateFlow()

    private var pendingImportPayments: List<ExportPayment> = emptyList()
    private var conflictPaymentIds: Map<Int, Long> = emptyMap() // index in pendingImport -> existing payment ID

    fun setThemeMode(mode: ThemeMode) {
        themePreferenceManager.setThemeMode(mode)
    }

    fun parseImportFile(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            try {
                val bytes = contentResolver.openInputStream(uri)?.use { it.readBytes() }
                    ?: throw Exception("Cannot read file")
                val payments = ExportImportManager.decode(bytes)

                if (payments.isEmpty()) {
                    _importState.update { it.copy(error = "No payments found in file") }
                    return@launch
                }

                pendingImportPayments = payments

                // Check conflicts
                val conflicts = mutableMapOf<Int, Long>()
                payments.forEachIndexed { index, ep ->
                    val existing = repository.findByVendorAndDate(ep.vendorName, ep.dueDateEpoch)
                    if (existing != null) {
                        conflicts[index] = existing.id
                    }
                }
                conflictPaymentIds = conflicts

                if (conflicts.isEmpty()) {
                    // No conflicts, import directly
                    doImport(replace = false)
                } else {
                    _importState.update {
                        ImportState(
                            showConflictDialog = true,
                            totalCount = payments.size,
                            newCount = payments.size - conflicts.size,
                            conflictCount = conflicts.size
                        )
                    }
                }
            } catch (e: Exception) {
                _importState.update { it.copy(error = e.message ?: "Invalid file") }
            }
        }
    }

    fun importKeepCurrent() {
        viewModelScope.launch {
            _importState.update { it.copy(showConflictDialog = false) }
            doImport(replace = false)
        }
    }

    fun importReplace() {
        viewModelScope.launch {
            _importState.update { it.copy(showConflictDialog = false) }
            doImport(replace = true)
        }
    }

    fun dismissConflictDialog() {
        _importState.update { ImportState() }
        pendingImportPayments = emptyList()
        conflictPaymentIds = emptyMap()
    }

    fun clearImportState() {
        _importState.update { ImportState() }
    }

    private suspend fun doImport(replace: Boolean) {
        try {
            var importedCount = 0

            pendingImportPayments.forEachIndexed { index, ep ->
                val existingId = conflictPaymentIds[index]

                if (existingId != null && !replace) {
                    return@forEachIndexed // skip this one
                }

                if (existingId != null && replace) {
                    val oldNotifs = repository.getNotificationsForPayment(existingId)
                    oldNotifs.forEach { notificationScheduler.cancel(it.id.toInt()) }
                    repository.deletePaymentById(existingId)
                }

                val dueDate = LocalDate.ofEpochDay(ep.dueDateEpoch)
                val payment = Payment(
                    vendorName = ep.vendorName,
                    invoiceNumber = ep.invoiceNumber,
                    dueDate = dueDate,
                    amount = ep.amount,
                    isPaid = ep.isPaid,
                    paidDate = ep.paidDateEpoch?.let { LocalDate.ofEpochDay(it) },
                    isMonthlyReminder = ep.isMonthlyReminder,
                    createdAt = ep.createdAt
                )

                val newId = repository.addPayment(payment)

                // Import custom notifications (only future ones get scheduled)
                if (ep.customNotifications.isNotEmpty()) {
                    val notifIds = repository.addNotifications(newId, ep.customNotifications, isCustom = true)
                    notifIds.zip(ep.customNotifications).forEach { (notifId, triggerMillis) ->
                        notificationScheduler.schedule(
                            notifId = notifId.toInt(),
                            vendorName = payment.vendorName,
                            amount = payment.amount,
                            dueDate = dueDate,
                            triggerMillis = triggerMillis
                        )
                    }
                }

                // Regenerate fresh monthly reminders (next 12 months, one day before due date)
                if (ep.isMonthlyReminder) {
                    val now = LocalDateTime.now()
                    val schedules = mutableListOf<Long>()
                    for (i in 1..12) {
                        val targetDate = try {
                            dueDate.plusMonths(i.toLong()).minusDays(1)
                        } catch (e: Exception) { continue }
                        val triggerDt = LocalDateTime.of(targetDate, LocalTime.of(9, 0))
                        if (triggerDt.isAfter(now)) {
                            schedules.add(triggerDt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                        }
                    }
                    if (schedules.isNotEmpty()) {
                        val monthlyIds = repository.addNotifications(newId, schedules)
                        monthlyIds.zip(schedules).forEach { (notifId, epochMillis) ->
                            notificationScheduler.schedule(
                                notifId = notifId.toInt(),
                                vendorName = payment.vendorName,
                                amount = payment.amount,
                                dueDate = dueDate,
                                triggerMillis = epochMillis
                            )
                        }
                    }
                }

                importedCount++
            }

            _importState.update {
                ImportState(importSuccess = true, importedCount = importedCount)
            }
        } catch (e: Exception) {
            _importState.update { it.copy(error = e.message ?: "Import failed") }
        } finally {
            pendingImportPayments = emptyList()
            conflictPaymentIds = emptyMap()
        }
    }
}
