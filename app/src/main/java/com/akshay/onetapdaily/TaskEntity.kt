package com.akshay.onetapdaily

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String,

    val completed: Boolean = false,

    val taskType: String = TaskType.HABIT.name,

    val archived: Boolean = false,

    val intervalDays: Int = 0,

    val nextDueDate: Long = 0,

    val createdAt: Long = System.currentTimeMillis(),

    val lastCompletedAt: Long = 0
)