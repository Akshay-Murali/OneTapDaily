package com.akshay.onetapdaily

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.*

data class Medicine(
    val name: String,
    val taken: Boolean = false,
    val takenTime: String? = null
)

@Composable
fun MedicinesScreen() {

    var medicines by remember {
        mutableStateOf(
            listOf(
                Medicine("Metformin"),
                Medicine("Vitamin D"),
                Medicine("Fish Oil")
            )
        )
    }

    var newMedicineName by remember {
        mutableStateOf("")
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

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {

            items(medicines) { medicine ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable {

                            val currentTime =
                                SimpleDateFormat(
                                    "h:mm a",
                                    Locale.getDefault()
                                ).format(Date())

                            medicines = medicines.map {

                                if (it.name == medicine.name) {

                                    if (it.taken)
                                        it.copy(
                                            taken = false,
                                            takenTime = null
                                        )
                                    else
                                        it.copy(
                                            taken = true,
                                            takenTime = currentTime
                                        )

                                } else {
                                    it
                                }
                            }
                        }
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text =
                                if (medicine.taken)
                                    "☑ ${medicine.name}"
                                else
                                    "☐ ${medicine.name}"
                        )

                        if (medicine.takenTime != null) {

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = "Taken at ${medicine.takenTime}"
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = newMedicineName,
            onValueChange = {
                newMedicineName = it
            },
            label = {
                Text("New Medicine")
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {

                if (newMedicineName.isNotBlank()) {

                    medicines =
                        medicines + Medicine(newMedicineName)

                    newMedicineName = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💊 Add Medicine")
        }
    }
}