package com.akshay.onetapdaily

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicine_logs")
data class MedicineLogEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val medicineId: Long,

    val takenAt: Long
)