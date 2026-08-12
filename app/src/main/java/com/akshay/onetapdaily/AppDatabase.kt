package com.akshay.onetapdaily

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        TaskEntity::class,
        SettingsEntity::class,
        MedicineEntity::class,
        MedicineLogEntity::class
    ],
    version = 8
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun taskDao(): TaskDao

    abstract fun settingsDao(): SettingsDao

    abstract fun medicineDao(): MedicineDao
}