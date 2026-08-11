package com.akshay.onetapdaily

class TaskRepository(
    private val taskDao: TaskDao
) {

    suspend fun getAllTasks(): List<TaskEntity> {
        return taskDao.getActiveTasks()
    }

    suspend fun insertTask(
        name: String,
        taskType: TaskType,
        intervalDays: Int = 0
    ) {

        taskDao.insertTask(
            TaskEntity(
                name = name,
                taskType = taskType.name,
                intervalDays = intervalDays,

                nextDueDate =
                    if (taskType == TaskType.RECURRING)
                        System.currentTimeMillis()
                    else
                        0
            )
        )
    }

    suspend fun updateTask(
        task: TaskEntity
    ) {
        taskDao.updateTask(task)
    }

    suspend fun deleteTask(
        task: TaskEntity
    ) {
        taskDao.deleteTask(task)
    }

    fun isDue(
        task: TaskEntity
    ): Boolean {

        if (task.taskType != TaskType.RECURRING.name)
            return true

        return System.currentTimeMillis() >= task.nextDueDate
    }
}