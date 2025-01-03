package com.example.strongest.model

import com.google.firebase.database.IgnoreExtraProperties
import java.util.Date

@IgnoreExtraProperties
data class MessageModel(val sender: String = "", val msg: String = "", val readStatus: Boolean = false, val sendAt: String = "")