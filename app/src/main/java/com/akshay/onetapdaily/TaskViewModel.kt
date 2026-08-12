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

    private val settingsRepository =
        SettingsRepository(
            (application as OneTapDailyApp)
                .database
                .settingsDao()
        )

    private val _tasks =
        MutableStateFlow<List<TaskEntity>>(emptyList())

    val tasks: StateFlow<List<TaskEntity>>
        get() = _tasks

    init {

        viewModelScope.launch {

            repository.resetRecurringTasks()

            loadTasks()
        }
    }

    fun refreshTasks() {

        viewModelScope.launch {

            repository.resetRecurringTasks()

            loadTasks()
        }
    }

    fun loadTasks() {

        viewModelScope.launch {

            _tasks.value =
                repository.getAllTasks()
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
                        completed = !task.completed,
                        lastCompletedAt =
                            if (!task.completed)
                                System.currentTimeMillis()
                            else
                                0
                    )
                )

                // HABIT STREAK LOGIC

                if (
                    task.taskType == TaskType.HABIT.name &&
                    !task.completed
                ) {

                    val updatedTasks =
                        repository.getAllTasks()

                    val habitTasks =
                        updatedTasks.filter {
                            it.taskType == TaskType.HABIT.name
                        }

                    val allHabitsDone =
                        habitTasks.isNotEmpty() &&
                                habitTasks.all { it.completed }

                    if (allHabitsDone) {

                        val settings =
                            settingsRepository.getSettings()
                                ?: SettingsEntity()

                        settingsRepository.saveSettings(
                            settings.copy(
                                streak = settings.streak + 1
                            )
                        )
                    }
                }
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