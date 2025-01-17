package com.example.strongest.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.strongest.model.AchievedMessage
import kotlinx.coroutines.flow.Flow

@Dao
interface AchievedMessageDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMessages(msgItem: AchievedMessage)

    @Update
    suspend fun updateMessages(msgItem: AchievedMessage)

    @Delete
    suspend fun deleteMessages(msgItem: AchievedMessage)

    @Query("SELECT * FROM achieved_messages")
    fun getAllMessages() : Flow<List<AchievedMessage>>

}