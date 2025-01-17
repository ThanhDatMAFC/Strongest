package com.example.strongest.model

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class MessageModel(val sender: String = "", val msg: String = "", val readStatus: Boolean = false, val sendAt: String = "")