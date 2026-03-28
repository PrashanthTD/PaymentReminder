package com.paymentreminder.ui.screens.payments

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

data class PaymentListUiState(
    val allPayments: List<Payment> = emptyList(),
    val filteredPayments: List<Payment> = emptyList(),
    val selectedFilter: PaymentStatus? = null
)

@HiltViewModel
class PaymentListViewModel @Inject constructor(
    private val repository: PaymentRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(PaymentListUiState())
    val uiState: StateFlow<PaymentListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getAllPayments().collect { payments ->
                _uiState.update { state ->
                    state.copy(
                        allPayments = payments,
                        filteredPayments = applyFilter(payments, state.selectedFilter)
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
        viewModelScope.launch { repository.deletePayment(payment) }
    }

    private fun applyFilter(payments: List<Payment>, status: PaymentStatus?): List<Payment> =
        if (status == null) payments else payments.filter { it.status == status }
}
