package com.example.strongest.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "achieved_messages")
data class AchievedMessage(
    @PrimaryKey
    val msgId: String,
    val chatId: String,
    @Embedded val message: MessageModel
)
