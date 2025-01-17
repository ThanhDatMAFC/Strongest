package com.example.strongest.data.repo

import com.example.strongest.data.dao.AchievedMessageDao
import com.example.strongest.model.AchievedMessage
import kotlinx.coroutines.flow.Flow

interface AchievedMessageRepository {
    fun getAllMessagesStream(): Flow<List<AchievedMessage>>

    suspend fun insertMessages(msgItem: AchievedMessage)

    suspend fun updateMessages(msgItem: AchievedMessage)

    suspend fun deleteMessages(msgItem: AchievedMessage)
}

class  LocalAchievedMessagesRepo(private val msgDao: AchievedMessageDao): AchievedMessageRepository {
    override fun getAllMessagesStream(): Flow<List<AchievedMessage>> = msgDao.getAllMessages()

    override suspend fun insertMessages(msgItem: AchievedMessage) = msgDao.insertMessages(msgItem)

    override suspend fun updateMessages(msgItem: AchievedMessage) = msgDao.updateMessages(msgItem)

    override suspend fun deleteMessages(msgItem: AchievedMessage) = msgDao.deleteMessages(msgItem)

}