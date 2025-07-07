package com.example.latihanuikeren

import com.google.firebase.Timestamp

data class Status (
    val id: String = "",
    val text: String = "",
    val userId: String = "",
    val userEmail: String = "",
    val timestamp: Timestamp? = null,
    val likeCount: Long = 0
)