package com.akshay.onetapdaily

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun MedicinesScreen() {

    val scope = rememberCoroutineScope()

    val viewModel: MedicineViewModel =
        viewModel()

    val medicines by
    viewModel.medicines.collectAsState()

    var newMedicineName by remember {
        mutableStateOf("")
    }

    var showAddMedicineDialog by remember {
        mutableStateOf(false)
    }

    var selectedMedicine by remember {
        mutableStateOf<MedicineEntity?>(null)
    }

    var medicineToDelete by remember {
        mutableStateOf<MedicineEntity?>(null)
    }

    var medicineHistory by remember {
        mutableStateOf<List<MedicineLogEntity>>(
            emptyList()
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Today's Medicines",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(
                items = medicines,
                key = { it.id }
            ) { medicine ->

                var lastTaken by remember(medicine.id) {
                    mutableStateOf("Never")
                }

                LaunchedEffect(medicine.id) {
                    lastTaken =
                        viewModel.getLastTakenText(
                            medicine.id
                        )
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = "💊 ${medicine.name}"
                        )

                        Spacer(
                            modifier = Modifier.height(4.dp)
                        )

                        Text(
                            text = "Last taken: $lastTaken",
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(
                            modifier = Modifier.height(12.dp)
                        )

                        Row {

                            Button(
                                onClick = {

                                    scope.launch {

                                        viewModel.markMedicineTaken(
                                            medicine
                                        )

                                        delay(100)

                                        lastTaken =
                                            viewModel.getLastTakenText(
                                                medicine.id
                                            )
                                    }
                                }
                            ) {
                                Text("Log")
                            }

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            OutlinedButton(
                                onClick = {

                                    scope.launch {

                                        medicineHistory =
                                            viewModel.getMedicineHistory(
                                                medicine.id
                                            )

                                        selectedMedicine =
                                            medicine
                                    }
                                }
                            ) {
                                Text("History")
                            }

                            Spacer(
                                modifier = Modifier.width(8.dp)
                            )

                            TextButton(
                                onClick = {
                                    medicineToDelete =
                                        medicine
                                }
                            ) {
                                Text("🗑")
                            }
                        }
                    }
                }
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                showAddMedicineDialog = true
            }
        ) {
            Text("💊 Add Medicine")
        }
    }

    // HISTORY DIALOG

    selectedMedicine?.let { medicine ->

        AlertDialog(

            onDismissRequest = {
                selectedMedicine = null
            },

            title = {
                Text("${medicine.name} History")
            },

            text = {

                Column {

                    if (
                        medicineHistory.isEmpty()
                    ) {

                        Text(
                            "No history yet"
                        )

                    } else {

                        medicineHistory.forEach { log ->

                            Text(
                                SimpleDateFormat(
                                    "MMM dd, yyyy h:mm a",
                                    Locale.getDefault()
                                ).format(
                                    Date(log.takenAt)
                                )
                            )

                            Spacer(
                                modifier =
                                    Modifier.height(4.dp)
                            )
                        }
                    }
                }
            },

            confirmButton = {

                TextButton(
                    onClick = {
                        selectedMedicine = null
                    }
                ) {
                    Text("Close")
                }
            }
        )
    }

    // DELETE DIALOG

    medicineToDelete?.let { medicine ->

        AlertDialog(

            onDismissRequest = {
                medicineToDelete = null
            },

            title = {
                Text("Delete Medicine")
            },

            text = {
                Text(
                    "Delete ${medicine.name}?"
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        viewModel.deleteMedicine(
                            medicine
                        )

                        medicineToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        medicineToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    // ADD MEDICINE DIALOG

    if (showAddMedicineDialog) {

        AlertDialog(

            onDismissRequest = {
                showAddMedicineDialog = false
            },

            title = {
                Text("Add Medicine")
            },

            text = {

                OutlinedTextField(

                    value = newMedicineName,

                    onValueChange = {
                        newMedicineName = it
                    },

                    label = {
                        Text("Medicine Name")
                    }
                )
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        if (
                            newMedicineName.isNotBlank()
                        ) {

                            viewModel.addMedicine(
                                newMedicineName
                            )

                            newMedicineName = ""

                            showAddMedicineDialog =
                                false
                        }
                    }
                ) {
                    Text("Add")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {

                        showAddMedicineDialog =
                            false

                        newMedicineName = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}