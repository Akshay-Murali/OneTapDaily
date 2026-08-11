package com.akshay.onetapdaily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TaskViewModel(
    application: Application
) : AndroidViewModel(application) {

    private val dao =
        (application as OneTapDailyApp)
            .database
            .taskDao()

    private val repository =
        TaskRepository(dao)

    private val _tasks =
        MutableStateFlow<List<TaskEntity>>(emptyList())

    val tasks: StateFlow<List<TaskEntity>>
        get() = _tasks

    init {
        loadTasks()
    }

    fun loadTasks() {

        viewModelScope.launch {

            _tasks.value = repository.getAllTasks()

        }
    }

    fun addTask(
        name: String,
        taskType: TaskType,
        intervalDays: Int = 0
    ) {

        viewModelScope.launch {

            repository.insertTask(
                name = name,
                taskType = taskType,
                intervalDays = intervalDays
            )

            loadTasks()
        }
    }

    fun toggleTask(
        task: TaskEntity
    ) {

        viewModelScope.launch {

            if (
                task.taskType == TaskType.RECURRING.name &&
                !task.completed
            ) {

                val now =
                    System.currentTimeMillis()

                val nextDueDate =
                    now +
                            (task.intervalDays * 24L * 60L * 60L * 1000L)

                repository.updateTask(
                    task.copy(
                        completed = true,
                        lastCompletedAt = now,
                        nextDueDate = nextDueDate
                    )
                )

            } else {

                repository.updateTask(
                    task.copy(
                        completed = !task.completed
                    )
                )
            }

            loadTasks()
        }
    }

    fun deleteTask(
        task: TaskEntity
    ) {

        viewModelScope.launch {

            repository.deleteTask(task)

            loadTasks()
        }
    }
}