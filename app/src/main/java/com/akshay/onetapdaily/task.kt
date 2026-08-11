package com.akshay.onetapdaily

import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val completed: Boolean = false
)