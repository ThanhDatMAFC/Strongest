package com.example.strongest.model

data class ChatItemModel(
    val friendId: String,
    val avatarUrl: String,
    val chatName: String,
    val message: String,
    val time: String,
    val readStatus: Boolean
)
