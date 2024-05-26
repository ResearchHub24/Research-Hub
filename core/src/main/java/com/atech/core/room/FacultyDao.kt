package com.atech.core.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.atech.core.room.model.FacultyRoomModel

@Dao
interface FacultyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFaculty(faculty: List<FacultyRoomModel>)

    @Query("SELECT * FROM faculty_table")
    suspend fun getFaculty(): List<FacultyRoomModel>

    @Query("DELETE FROM faculty_table")
    suspend fun deleteAllFaculty()
}