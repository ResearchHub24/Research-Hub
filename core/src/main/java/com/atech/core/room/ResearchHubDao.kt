package com.atech.core.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.atech.core.room.model.ResearchRoomModel
import kotlinx.coroutines.flow.Flow

@Dao
interface ResearchHubDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResearch(research: ResearchRoomModel)

    @Query("SELECT * FROM research_table ORDER BY addedAt DESC")
    fun getAllResearch(): Flow<List<ResearchRoomModel>>

    @Query("DELETE FROM research_table")
    suspend fun deleteAllResearch()

    @Query("SELECT * FROM research_table WHERE `key` = :key")
    suspend fun getResearchByKey(key: String): ResearchRoomModel?

    @Query("DELETE FROM research_table WHERE `key` = :key")
    suspend fun deleteResearchByKey(key: String)

}