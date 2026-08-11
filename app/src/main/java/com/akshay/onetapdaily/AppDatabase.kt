package com.akshay.onetapdaily

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TaskEntity::class,
        SettingsEntity::class
    ],
    version = 7
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun settingsDao(): SettingsDao
}