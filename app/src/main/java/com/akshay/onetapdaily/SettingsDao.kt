package com.akshay.onetapdaily

import androidx.room.*

@Dao
interface SettingsDao {

    @Query("SELECT * FROM settings WHERE id = 1")
    suspend fun getSettings(): SettingsEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveSettings(
        settings: SettingsEntity
    )
}