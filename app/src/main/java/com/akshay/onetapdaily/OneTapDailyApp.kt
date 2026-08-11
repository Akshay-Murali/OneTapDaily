package com.akshay.onetapdaily

import android.app.Application
import androidx.room.Room

class OneTapDailyApp : Application() {

    lateinit var database: AppDatabase

    override fun onCreate() {
        super.onCreate()

        database =
            Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "onetapdaily.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }
}