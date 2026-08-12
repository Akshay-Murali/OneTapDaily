package com.akshay.onetapdaily

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TasksScreen() {

    val viewModel: TaskViewModel = viewModel()

    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {

        val observer =
            LifecycleEventObserver { _, event ->

                if (event == Lifecycle.Event.ON_RESUME) {
                    viewModel.refreshTasks()
                }
            }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val tasks by viewModel.tasks.collectAsState()

    var showAddDialog by remember {
        mutableStateOf(false)
    }

    var taskToDelete by remember {
        mutableStateOf<TaskEntity?>(null)
    }

    val habitTasks =
        tasks.filter {
            it.taskType == TaskType.HABIT.name
        }

    val completedCount =
        habitTasks.count {
            it.completed
        }

    val totalCount =
        habitTasks.size

    val remainingCount =
        totalCount - completedCount

    val progressPercent =
        if (totalCount == 0)
            0
        else
            (completedCount * 100) / totalCount

    val streak =
        if (progressPercent == 100 && totalCount > 0)
            1
        else
            0

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Text(
            text = "Today's Progress",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth()
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),

                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                AnimatedProgressRing(
                    percentage = progressPercent
                )

                Column {

                    Text(
                        text = "🔥 $streak Day Streak",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(
                        modifier = Modifier.height(8.dp)
                    )

                    Text(
                        "$completedCount / $totalCount Habits Complete"
                    )

                    Text(
                        "$remainingCount Habits Remaining"
                    )
                }
            }
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Today's Tasks",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            items(
                items = tasks,
                key = { it.id }
            ) { task ->

                TaskCard(
                    task = task,

                    onToggle = {
                        viewModel.toggleTask(task)
                    },

                    onDelete = {
                        taskToDelete = task
                    }
                )
            }
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            modifier = Modifier.fillMaxWidth(),

            onClick = {
                showAddDialog = true
            }
        ) {
            Text("➕ Add Task")
        }
    }

    taskToDelete?.let { task ->

        AlertDialog(

            onDismissRequest = {
                taskToDelete = null
            },

            title = {
                Text("Delete Task")
            },

            text = {
                Text("Delete '${task.name}'?")
            },

            confirmButton = {

                TextButton(
                    onClick = {

                        viewModel.deleteTask(task)

                        taskToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        taskToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showAddDialog) {

        AddTaskDialog(

            onDismiss = {
                showAddDialog = false
            },

            onCreateTask = { name, type, intervalDays ->

                viewModel.addTask(
                    name = name,
                    taskType = type,
                    intervalDays = intervalDays
                )

                showAddDialog = false
            }
        )
    }
}