package com.paymentreminder.ui.screens.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.paymentreminder.data.model.Payment
import com.paymentreminder.data.model.PaymentStatus
import com.paymentreminder.data.repository.PaymentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class DashboardUiState(
    val totalCount: Int = 0,
    val overdueCount: Int = 0,
    val dueSoonCount: Int = 0,
    val paidCount: Int = 0,
    val totalUnpaidAmount: Double = 0.0,
    val urgentPayments: List<Payment> = emptyList()
)

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getTotalCount().collect { count ->
                _uiState.update { it.copy(totalCount = count) }
            }
        }
        viewModelScope.launch {
            repository.getOverdueCount().collect { count ->
                _uiState.update { it.copy(overdueCount = count) }
            }
        }
        viewModelScope.launch {
            repository.getDueSoonCount().collect { count ->
                _uiState.update { it.copy(dueSoonCount = count) }
            }
        }
        viewModelScope.launch {
            repository.getPaidCount().collect { count ->
                _uiState.update { it.copy(paidCount = count) }
            }
        }
        viewModelScope.launch {
            repository.getTotalUnpaidAmount().collect { amount ->
                _uiState.update { it.copy(totalUnpaidAmount = amount) }
            }
        }
        viewModelScope.launch {
            repository.getAllPayments().collect { payments ->
                val urgent = payments
                    .filter { it.status == PaymentStatus.OVERDUE || it.status == PaymentStatus.DUE_SOON }
                    .take(5)
                _uiState.update { it.copy(urgentPayments = urgent) }
            }
        }
    }

    fun markAsPaid(paymentId: Long) {
        viewModelScope.launch { repository.markAsPaid(paymentId) }
    }
}
