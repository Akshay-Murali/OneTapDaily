package com.akshay.onetapdaily

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onCreateTask: (
        String,
        TaskType,
        Int
    ) -> Unit
) {

    var taskName by remember {
        mutableStateOf("")
    }

    var selectedType by remember {
        mutableStateOf(TaskType.HABIT)
    }

    var intervalDays by remember {
        mutableStateOf("7")
    }

    AlertDialog(

        onDismissRequest = onDismiss,

        title = {
            Text("Create Task")
        },

        text = {

            Column {

                OutlinedTextField(
                    value = taskName,

                    onValueChange = {
                        taskName = it
                    },

                    label = {
                        Text("Task Name")
                    },

                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(
                    modifier = Modifier.height(16.dp)
                )

                Text("Task Type")

                Spacer(
                    modifier = Modifier.height(8.dp)
                )

                TaskType.entries.forEach { type ->

                    Row {

                        RadioButton(
                            selected = selectedType == type,

                            onClick = {
                                selectedType = type
                            }
                        )

                        Text(
                            text = type.name
                        )
                    }
                }

                if (selectedType == TaskType.RECURRING) {

                    Spacer(
                        modifier = Modifier.height(16.dp)
                    )

                    OutlinedTextField(
                        value = intervalDays,

                        onValueChange = {
                            intervalDays = it
                        },

                        label = {
                            Text("Repeat Every (Days)")
                        },

                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        },

        confirmButton = {

            TextButton(

                onClick = {

                    if (taskName.isNotBlank()) {

                        onCreateTask(
                            taskName,
                            selectedType,
                            if (selectedType == TaskType.RECURRING)
                                intervalDays.toIntOrNull() ?: 7
                            else
                                0
                        )
                    }
                }
            ) {
                Text("Create")
            }
        },

        dismissButton = {

            TextButton(
                onClick = onDismiss
            ) {
                Text("Cancel")
            }
        }
    )
}