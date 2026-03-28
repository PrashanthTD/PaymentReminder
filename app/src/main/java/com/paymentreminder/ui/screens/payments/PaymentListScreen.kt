package com.paymentreminder.ui.screens.payments

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.paymentreminder.data.model.PaymentStatus
import com.paymentreminder.ui.components.PaymentCard
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentListScreen(
    onAddPayment: () -> Unit,
    onEditPayment: (Long) -> Unit,
    viewModel: PaymentListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var showFilter by remember { mutableStateOf(false) }
    val context = LocalContext.current

    var showDeleteDialog by remember { mutableStateOf(false) }

    val exportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/octet-stream")
    ) { uri ->
        if (uri != null) {
            viewModel.exportSelected(uri, context.contentResolver)
        }
    }

    LaunchedEffect(uiState.exportDone) {
        if (uiState.exportDone) {
            Toast.makeText(context, "Exported successfully", Toast.LENGTH_SHORT).show()
            viewModel.clearExportState()
        }
    }

    LaunchedEffect(uiState.exportError) {
        uiState.exportError?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            viewModel.clearExportState()
        }
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            title = { Text("Delete ${uiState.selectedIds.size} payment(s)?", color = MaterialTheme.colorScheme.onSurface) },
            text = { Text("This will permanently delete the selected payments and all their notifications.", color = MaterialTheme.colorScheme.onSurfaceVariant) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.deleteSelected()
                    showDeleteDialog = false
                }) { Text("Delete", color = MaterialTheme.colorScheme.error) }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) { Text("Cancel") }
            }
        )
    }

    Scaffold(
        topBar = {
            if (uiState.selectionMode) {
                TopAppBar(
                    title = {
                        Text(
                            "${uiState.selectedIds.size} selected",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { viewModel.exitSelectionMode() }) {
                            Icon(Icons.Default.Close, contentDescription = "Cancel")
                        }
                    },
                    actions = {
                        val allSelected = uiState.selectedIds.size == uiState.filteredPayments.size
                                && uiState.filteredPayments.isNotEmpty()
                        IconButton(onClick = {
                            if (allSelected) viewModel.deselectAll() else viewModel.selectAll()
                        }) {
                            Icon(
                                Icons.Default.SelectAll,
                                contentDescription = if (allSelected) "Deselect All" else "Select All"
                            )
                        }
                        IconButton(
                            onClick = {
                                val fileName = "payment_backup_${
                                    LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd"))
                                }.prb"
                                exportLauncher.launch(fileName)
                            },
                            enabled = uiState.selectedIds.isNotEmpty()
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Export")
                        }
                        IconButton(
                            onClick = { showDeleteDialog = true },
                            enabled = uiState.selectedIds.isNotEmpty()
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            } else {
                TopAppBar(
                    title = {
                        Text(
                            "Payments",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    actions = {
                        IconButton(onClick = { showFilter = !showFilter }) {
                            Icon(
                                Icons.Default.FilterList,
                                contentDescription = "Filter",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        titleContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        floatingActionButton = {
            if (!uiState.selectionMode) {
                FloatingActionButton(
                    onClick = onAddPayment,
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Add Payment")
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            AnimatedVisibility(visible = showFilter && !uiState.selectionMode) {
                val filters = listOf<Pair<String, PaymentStatus?>>("All" to null) +
                    PaymentStatus.entries.map { it.name.replace("_", " ") to it }

                SingleChoiceSegmentedButtonRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    filters.forEachIndexed { index, (label, status) ->
                        SegmentedButton(
                            selected = uiState.selectedFilter == status,
                            onClick = { viewModel.setFilter(status) },
                            shape = SegmentedButtonDefaults.itemShape(index, filters.size),
                            label = { Text(label, maxLines = 1, style = MaterialTheme.typography.labelSmall) }
                        )
                    }
                }
            }

            if (uiState.filteredPayments.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No payments found",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(items = uiState.filteredPayments, key = { it.id }) { payment ->
                        PaymentCard(
                            payment = payment,
                            onMarkPaid = { viewModel.markAsPaid(payment.id) },
                            onEdit = { onEditPayment(payment.id) },
                            onDelete = { viewModel.deletePayment(payment) },
                            selectionMode = uiState.selectionMode,
                            isSelected = payment.id in uiState.selectedIds,
                            onLongPress = {
                                if (!uiState.selectionMode) {
                                    viewModel.enterSelectionMode(payment.id)
                                }
                            },
                            onSelectionToggle = { viewModel.toggleSelection(payment.id) }
                        )
                    }
                }
            }
        }
    }
}
