package com.akshay.onetapdaily

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

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

    private val _streak =
        MutableStateFlow(0)

    val tasks: StateFlow<List<TaskEntity>>
        get() = _tasks

    val streak: StateFlow<Int>
        get() = _streak

    init {

        viewModelScope.launch {

            repository.resetRecurringTasks()

            resetStreakIfMissedDay()

            loadTasks()
        }
    }

    fun refreshTasks() {

        viewModelScope.launch {

            repository.resetRecurringTasks()

            resetStreakIfMissedDay()

            loadTasks()
        }
    }

    fun loadTasks() {

        viewModelScope.launch {

            _tasks.value =
                repository.getAllTasks()

            loadStreak()
        }
    }

    private suspend fun loadStreak() {

        val settings =
            settingsRepository.getSettings()
                ?: SettingsEntity()

        _streak.value =
            settings.streak
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
            }

            updateStreakIfNeeded()

            loadTasks()
        }
    }

    private suspend fun updateStreakIfNeeded() {

        val tasks =
            repository.getAllTasks()

        val habitTasks =
            tasks.filter {
                it.taskType == TaskType.HABIT.name
            }

        if (habitTasks.isEmpty())
            return

        val allHabitsDone =
            habitTasks.all {
                it.completed
            }

        if (!allHabitsDone)
            return

        val today =
            DailyResetManager()
                .getToday()

        val settings =
            settingsRepository.getSettings()
                ?: SettingsEntity()

        if (settings.lastResetDate != today) {

            val updatedSettings =
                settings.copy(
                    streak = settings.streak + 1,
                    lastResetDate = today
                )

            settingsRepository.saveSettings(
                updatedSettings
            )

            _streak.value =
                updatedSettings.streak
        }
    }

    private suspend fun resetStreakIfMissedDay() {

        val settings =
            settingsRepository.getSettings()
                ?: return

        if (settings.lastResetDate.isBlank())
            return

        val formatter =
            SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            )

        val lastDate =
            formatter.parse(
                settings.lastResetDate
            ) ?: return

        val today =
            formatter.parse(
                DailyResetManager().getToday()
            ) ?: return

        val diffDays =
            (today.time - lastDate.time) /
                    (24L * 60L * 60L * 1000L)

        if (diffDays > 1) {

            settingsRepository.saveSettings(
                settings.copy(
                    streak = 0
                )
            )

            _streak.value = 0
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