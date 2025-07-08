package com.example.threadapp.model

data class NotificationModel(
    val type: String = "",
    val username : String = "",
    val fromUid: String = "",      // UID of the user who triggered it
    val timestamp: Long = 0L
)