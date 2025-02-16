package com.atech.core.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.atech.core.room.model.FacultyRoomModel
import com.atech.core.room.model.ResearchRoomModel

@Database(
    entities = [
        ResearchRoomModel::class,
        FacultyRoomModel::class
    ], version = 1
)
abstract class ResearchHubDatabase : RoomDatabase() {
    abstract fun researchHubDao(): ResearchHubDao
    abstract fun facultyDao(): FacultyDao

    companion object {
        const val DATABASE_NAME = "research_hub_db"
    }
}