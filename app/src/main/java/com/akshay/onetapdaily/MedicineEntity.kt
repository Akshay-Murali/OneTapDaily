package com.akshay.onetapdaily

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class MedicineEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    val name: String
)