package com.akshay.onetapdaily

class SettingsRepository(
    private val settingsDao: SettingsDao
) {

    suspend fun getSettings(): SettingsEntity? {
        return settingsDao.getSettings()
    }

    suspend fun saveSettings(
        settings: SettingsEntity
    ) {
        settingsDao.saveSettings(settings)
    }
}