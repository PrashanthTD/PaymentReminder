package com.paymentreminder.ui.screens.addpayment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentreminder.data.model.Payment
import com.paymentreminder.data.repository.PaymentRepository
import com.paymentreminder.notification.NotificationHelper
import com.paymentreminder.notification.NotificationScheduler
import com.paymentreminder.util.DateUtils
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

data class AddPaymentUiState(
    val vendorName: String = "",
    val invoiceNumber: String = "",
    val amount: String = "",
    val dueDate: LocalDate? = null,
    val dueDateDisplay: String = "",
    val vendorNameError: String? = null,
    val amountError: String? = null,
    val dueDateError: String? = null,
    val notifications: List<NotificationDraft> = emptyList(),
    val monthlyReminder: Boolean = false,
    val isSaving: Boolean = false,
    val saved: Boolean = false
)

@HiltViewModel
class AddPaymentViewModel @Inject constructor(
    private val repository: PaymentRepository,
    private val notificationHelper: NotificationHelper,
    private val notificationScheduler: NotificationScheduler
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddPaymentUiState())
    val uiState: StateFlow<AddPaymentUiState> = _uiState.asStateFlow()

    fun updateVendorName(value: String) =
        _uiState.update { it.copy(vendorName = value, vendorNameError = null) }

    fun updateInvoiceNumber(value: String) =
        _uiState.update { it.copy(invoiceNumber = value) }

    fun updateAmount(value: String) =
        _uiState.update { it.copy(amount = value, amountError = null) }

    fun updateDueDate(date: LocalDate) =
        _uiState.update {
            it.copy(
                dueDate = date,
                dueDateDisplay = DateUtils.formatForDisplay(date),
                dueDateError = null
            )
        }

    fun addNotification(dateTime: LocalDateTime) =
        _uiState.update { it.copy(notifications = it.notifications + NotificationDraft(dateTime = dateTime)) }

    fun updateNotification(localId: String, dateTime: LocalDateTime) =
        _uiState.update {
            it.copy(notifications = it.notifications.map { n ->
                if (n.localId == localId) n.copy(dateTime = dateTime) else n
            })
        }

    fun removeNotification(localId: String) =
        _uiState.update { it.copy(notifications = it.notifications.filter { n -> n.localId != localId }) }

    fun toggleMonthlyReminder() =
        _uiState.update { it.copy(monthlyReminder = !it.monthlyReminder) }

    fun save() {
        val state = _uiState.value
        var hasError = false

        if (state.vendorName.isBlank()) {
            _uiState.update { it.copy(vendorNameError = "Vendor name is required") }
            hasError = true
        }

        var amountValue = 0.0
        if (state.amount.isNotBlank()) {
            val parsed = state.amount.toDoubleOrNull()
            if (parsed == null) {
                _uiState.update { it.copy(amountError = "Enter a valid amount") }
                hasError = true
            } else {
                amountValue = parsed
            }
        }

        if (state.dueDate == null) {
            _uiState.update { it.copy(dueDateError = "Select a due date") }
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }

            val payment = Payment(
                vendorName = state.vendorName.trim(),
                invoiceNumber = state.invoiceNumber.trim(),
                dueDate = state.dueDate!!,
                amount = amountValue,
                isMonthlyReminder = state.monthlyReminder
            )
            val insertedId = repository.addPayment(payment)
            val savedPayment = payment.copy(id = insertedId)

            notificationHelper.showPaymentAdded(savedPayment)

            // Schedule individual notifications
            if (state.notifications.isNotEmpty()) {
                val epochList = state.notifications.map { it.epochMillis }
                val notifIds = repository.addNotifications(insertedId, epochList, isCustom = true)
                notifIds.zip(state.notifications).forEach { (notifId, draft) ->
                    notificationScheduler.schedule(
                        notifId = notifId.toInt(),
                        vendorName = savedPayment.vendorName,
                        amount = savedPayment.amount,
                        dueDate = savedPayment.dueDate,
                        triggerMillis = draft.epochMillis
                    )
                }
            }

            // Schedule monthly reminders for the next 12 months at 9:00 AM, one day before the due date
            if (state.monthlyReminder) {
                val dueDate = state.dueDate!!
                val now = LocalDateTime.now()
                val schedules = mutableListOf<Long>()
                for (i in 1..12) {
                    val targetDate = try { dueDate.plusMonths(i.toLong()).minusDays(1) } catch (e: Exception) { continue }
                    val triggerDt = LocalDateTime.of(targetDate, LocalTime.of(9, 0))
                    if (triggerDt.isAfter(now)) {
                        schedules.add(triggerDt.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli())
                    }
                }
                if (schedules.isNotEmpty()) {
                    val monthlyIds = repository.addNotifications(insertedId, schedules)
                    monthlyIds.zip(schedules).forEach { (notifId, epochMillis) ->
                        notificationScheduler.schedule(
                            notifId = notifId.toInt(),
                            vendorName = savedPayment.vendorName,
                            amount = savedPayment.amount,
                            dueDate = savedPayment.dueDate,
                            triggerMillis = epochMillis
                        )
                    }
                }
            }

            _uiState.update { it.copy(isSaving = false, saved = true) }
        }
    }
}
