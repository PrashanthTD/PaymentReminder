package com.paymentreminder.ui.screens.addpayment

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddAlarm
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId

private enum class PickerPhase { NONE, DATE, TIME }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPaymentScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    viewModel: AddPaymentViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Due date picker
    var showDueDatePicker by remember { mutableStateOf(false) }
    val dueDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis() + 86_400_000L
    )

    // Notification picker two-step state
    var notifPickerPhase by remember { mutableStateOf(PickerPhase.NONE) }
    var notifPickerDate by remember { mutableStateOf<LocalDate?>(null) }
    var editingNotifId by remember { mutableStateOf<String?>(null) }

    val notifDatePickerState = rememberDatePickerState(
        initialSelectedDateMillis = System.currentTimeMillis()
    )
    val notifTimePickerState = rememberTimePickerState(
        initialHour = 9, initialMinute = 0, is24Hour = false
    )

    LaunchedEffect(uiState.saved) {
        if (uiState.saved) onSaved()
    }

    // ── Due date picker dialog ────────────────────────────────────────────
    if (showDueDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDueDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    dueDatePickerState.selectedDateMillis?.let { millis ->
                        viewModel.updateDueDate(
                            Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate()
                        )
                    }
                    showDueDatePicker = false
                }) { Text("OK") }
            },
            dismissButton = { TextButton(onClick = { showDueDatePicker = false }) { Text("Cancel") } }
        ) { DatePicker(state = dueDatePickerState) }
    }

    // ── Notification – step 1: date picker ───────────────────────────────
    if (notifPickerPhase == PickerPhase.DATE) {
        DatePickerDialog(
            onDismissRequest = { notifPickerPhase = PickerPhase.NONE; editingNotifId = null },
            confirmButton = {
                TextButton(onClick = {
                    notifDatePickerState.selectedDateMillis?.let { millis ->
                        notifPickerDate = Instant.ofEpochMilli(millis)
                            .atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    notifPickerPhase = PickerPhase.TIME
                }) { Text("Next") }
            },
            dismissButton = {
                TextButton(onClick = { notifPickerPhase = PickerPhase.NONE; editingNotifId = null }) {
                    Text("Cancel")
                }
            }
        ) { DatePicker(state = notifDatePickerState) }
    }

    // ── Notification – step 2: time picker ───────────────────────────────
    if (notifPickerPhase == PickerPhase.TIME) {
        AlertDialog(
            onDismissRequest = { notifPickerPhase = PickerPhase.NONE; editingNotifId = null },
            title = { Text("Select time") },
            text = { TimePicker(state = notifTimePickerState) },
            confirmButton = {
                TextButton(onClick = {
                    val date = notifPickerDate ?: LocalDate.now()
                    val dt = LocalDateTime.of(date, LocalTime.of(notifTimePickerState.hour, notifTimePickerState.minute))
                    val id = editingNotifId
                    if (id == null) {
                        viewModel.addNotification(dt)
                    } else {
                        viewModel.updateNotification(id, dt)
                    }
                    notifPickerPhase = PickerPhase.NONE
                    editingNotifId = null
                    notifPickerDate = null
                }) { Text("Done") }
            },
            dismissButton = {
                TextButton(onClick = { notifPickerPhase = PickerPhase.NONE; editingNotifId = null }) {
                    Text("Cancel")
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Add Payment", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.vendorName,
                onValueChange = viewModel::updateVendorName,
                label = { Text("Vendor Name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = uiState.vendorNameError != null,
                supportingText = uiState.vendorNameError?.let { { Text(it) } }
            )

            OutlinedTextField(
                value = uiState.invoiceNumber,
                onValueChange = viewModel::updateInvoiceNumber,
                label = { Text("Invoice Number (Optional)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            OutlinedTextField(
                value = uiState.amount,
                onValueChange = viewModel::updateAmount,
                label = { Text("Amount (Optional)") },
                prefix = { Text("\u20B9") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = uiState.amountError != null,
                supportingText = uiState.amountError?.let { { Text(it) } }
            )

            OutlinedTextField(
                value = uiState.dueDateDisplay,
                onValueChange = {},
                label = { Text("Due Date") },
                modifier = Modifier.fillMaxWidth(),
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { showDueDatePicker = true }) {
                        Icon(Icons.Default.CalendarMonth, contentDescription = "Select date")
                    }
                },
                isError = uiState.dueDateError != null,
                supportingText = uiState.dueDateError?.let { { Text(it) } }
            )

            // ── Notifications section ─────────────────────────────────────
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {

                    // Header row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.NotificationsActive,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "Notifications",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        )
                        TextButton(onClick = { notifPickerPhase = PickerPhase.DATE; editingNotifId = null }) {
                            Icon(Icons.Default.AddAlarm, contentDescription = null, modifier = Modifier.size(16.dp))
                            Text(" Add", style = MaterialTheme.typography.labelMedium)
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 2.dp))

                    // Monthly reminder toggle
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            Icons.Default.Autorenew,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.secondary,
                            modifier = Modifier.size(20.dp)
                        )
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 8.dp)
                        ) {
                            Text(
                                text = "Monthly Reminder",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = "Notify every month on due date at 9:00 AM",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.monthlyReminder,
                            onCheckedChange = { viewModel.toggleMonthlyReminder() }
                        )
                    }

                    // Individual notifications list
                    if (uiState.notifications.isNotEmpty()) {
                        HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                        uiState.notifications.forEachIndexed { index, notif ->
                            if (index > 0) HorizontalDivider()
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = notif.displayText,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                                TextButton(onClick = {
                                    editingNotifId = notif.localId
                                    notifPickerPhase = PickerPhase.DATE
                                }) { Text("Edit", style = MaterialTheme.typography.labelSmall) }
                                IconButton(
                                    onClick = { viewModel.removeNotification(notif.localId) },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = MaterialTheme.colorScheme.error,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    } else if (!uiState.monthlyReminder) {
                        Text(
                            text = "No notifications set",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 4.dp, top = 4.dp, bottom = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = viewModel::save,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Save Payment", style = MaterialTheme.typography.labelLarge)
                }
            }
        }
    }
}
