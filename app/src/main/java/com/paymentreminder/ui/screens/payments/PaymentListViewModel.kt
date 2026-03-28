package com.paymentreminder.ui.screens.payments

import android.content.ContentResolver
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentreminder.data.export.ExportImportManager
import com.paymentreminder.data.export.ExportPayment
import com.paymentreminder.data.model.Payment
import com.paymentreminder.data.model.PaymentStatus
import com.paymentreminder.data.repository.PaymentRepository
import com.paymentreminder.notification.NotificationScheduler
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class PaymentListUiState(
    val allPayments: List<Payment> = emptyList(),
    val filteredPayments: List<Payment> = emptyList(),
    val selectedFilter: PaymentStatus? = null,
    val selectionMode: Boolean = false,
    val selectedIds: Set<Long> = emptySet(),
    val exportDone: Boolean = false,
    val exportError: String? = null
)

@HiltViewModel
class PaymentListViewModel @Inject constructor(
    private val repository: PaymentRepository,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentListUiState())
    val uiState: StateFlow<PaymentListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllPayments().collect { payments ->
                _uiState.update { state ->
                    state.copy(
                        allPayments = payments,
                        filteredPayments = applyFilter(payments, state.selectedFilter),
                        selectedIds = state.selectedIds.filter { id -> payments.any { it.id == id } }.toSet()
                    )
                }
            }
        }
    }

    fun setFilter(status: PaymentStatus?) {
        _uiState.update { state ->
            state.copy(
                selectedFilter = status,
                filteredPayments = applyFilter(state.allPayments, status)
            )
        }
    }

    fun markAsPaid(paymentId: Long) {
        viewModelScope.launch { repository.markAsPaid(paymentId) }
    }

    fun deletePayment(payment: Payment) {
        viewModelScope.launch {
            // Cancel all scheduled alarms before deleting
            val notifs = repository.getNotificationsForPayment(payment.id)
            notifs.forEach { notificationScheduler.cancel(it.id.toInt()) }
            repository.deletePayment(payment)
        }
    }

    fun deleteSelected() {
        viewModelScope.launch {
            val state = _uiState.value
            val toDelete = state.allPayments.filter { it.id in state.selectedIds }
            for (payment in toDelete) {
                val notifs = repository.getNotificationsForPayment(payment.id)
                notifs.forEach { notificationScheduler.cancel(it.id.toInt()) }
                repository.deletePayment(payment)
            }
            _uiState.update { it.copy(selectionMode = false, selectedIds = emptySet()) }
        }
    }

    // ── Selection mode ───────────────────────────────────────────────────

    fun enterSelectionMode(paymentId: Long) {
        _uiState.update { it.copy(selectionMode = true, selectedIds = setOf(paymentId)) }
    }

    fun exitSelectionMode() {
        _uiState.update { it.copy(selectionMode = false, selectedIds = emptySet()) }
    }

    fun toggleSelection(paymentId: Long) {
        _uiState.update { state ->
            val newSet = if (paymentId in state.selectedIds) {
                state.selectedIds - paymentId
            } else {
                state.selectedIds + paymentId
            }
            val stillInMode = newSet.isNotEmpty()
            state.copy(selectedIds = newSet, selectionMode = stillInMode)
        }
    }

    fun selectAll() {
        _uiState.update { state ->
            state.copy(selectedIds = state.filteredPayments.map { it.id }.toSet())
        }
    }

    fun deselectAll() {
        _uiState.update { it.copy(selectedIds = emptySet()) }
    }

    // ── Export ────────────────────────────────────────────────────────────

    fun exportSelected(uri: Uri, contentResolver: ContentResolver) {
        viewModelScope.launch {
            try {
                val state = _uiState.value
                val selectedPayments = state.allPayments.filter { it.id in state.selectedIds }

                val exportList = selectedPayments.map { payment ->
                    val customNotifs = repository.getCustomNotificationsForPayment(payment.id)
                        .map { it.scheduledAt }
                    ExportPayment(
                        vendorName = payment.vendorName,
                        invoiceNumber = payment.invoiceNumber,
                        dueDateEpoch = payment.dueDate.toEpochDay(),
                        amount = payment.amount,
                        isPaid = payment.isPaid,
                        paidDateEpoch = payment.paidDate?.toEpochDay(),
                        isMonthlyReminder = payment.isMonthlyReminder,
                        customNotifications = customNotifs,
                        createdAt = payment.createdAt
                    )
                }

                val encoded = ExportImportManager.encode(exportList)
                contentResolver.openOutputStream(uri)?.use { it.write(encoded) }

                _uiState.update { it.copy(exportDone = true, selectionMode = false, selectedIds = emptySet()) }
            } catch (e: Exception) {
                _uiState.update { it.copy(exportError = e.message ?: "Export failed") }
            }
        }
    }

    fun clearExportState() {
        _uiState.update { it.copy(exportDone = false, exportError = null) }
    }

    private fun applyFilter(payments: List<Payment>, status: PaymentStatus?): List<Payment> =
        if (status == null) payments else payments.filter { it.status == status }
}
