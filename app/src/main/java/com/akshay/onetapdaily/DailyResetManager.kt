package com.akshay.onetapdaily

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class DailyResetManager {

    fun getToday(): String {

        return SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        ).format(Date())
    }
}