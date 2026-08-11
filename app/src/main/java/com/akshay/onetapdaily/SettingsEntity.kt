package com.akshay.onetapdaily

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "settings")
data class SettingsEntity(

    @PrimaryKey
    val id: Int = 1,

    val streak: Int = 0,

    val lastResetDate: String = ""
)