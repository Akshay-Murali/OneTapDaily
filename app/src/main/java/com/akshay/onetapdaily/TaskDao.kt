package com.akshay.onetapdaily

import androidx.room.*

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks WHERE archived = 0")
    suspend fun getActiveTasks(): List<TaskEntity>

    @Insert
    suspend fun insertTask(task: TaskEntity)

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}