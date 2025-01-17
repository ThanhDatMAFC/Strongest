package com.example.strongest.data

import android.content.Context
import com.example.strongest.data.inventory.InventoryDatabase
import com.example.strongest.data.repo.AchievedMessageRepository
import com.example.strongest.data.repo.LocalAchievedMessagesRepo

interface DataContainer {
    val achievedMessageRepo : AchievedMessageRepository
}
class AppContainer(private val context: Context) : DataContainer {
    override val achievedMessageRepo: AchievedMessageRepository by lazy {
       LocalAchievedMessagesRepo(InventoryDatabase.getDatabase(context).achievedMessageDao())
    }
}