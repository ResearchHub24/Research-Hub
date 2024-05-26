package com.atech.core.room.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atech.core.retrofit.FacultyModel
import com.atech.core.utils.EntityMapper
import javax.inject.Inject

@Entity(tableName = "faculty_table")
data class FacultyRoomModel(
    val name: String,
    @PrimaryKey(autoGenerate = false) val email: String,
    val imageUrl: String,
    val profileData: String,
    val areaOfInterest: String,
    val profileUrl: String
)

class FacultyMapper @Inject constructor() : EntityMapper<FacultyModel, FacultyRoomModel> {
    override fun mapFromEntity(entity: FacultyModel): FacultyRoomModel =
        FacultyRoomModel(
            name = entity.name,
            email = entity.email,
            imageUrl = entity.imageUrl,
            profileData = entity.profileData,
            areaOfInterest = entity.areaOfInterest,
            profileUrl = entity.profileUrl
        )

    override fun mapToEntity(domainModel: FacultyRoomModel): FacultyModel =
        FacultyModel(
            name = domainModel.name,
            email = domainModel.email,
            imageUrl = domainModel.imageUrl,
            profileData = domainModel.profileData,
            areaOfInterest = domainModel.areaOfInterest,
            profileUrl = domainModel.profileUrl
        )

    fun mapFromEntityList(entities: List<FacultyModel>): List<FacultyRoomModel> =
        entities.map { mapFromEntity(it) }

    fun mapToEntityList(entities: List<FacultyRoomModel>): List<FacultyModel> =
        entities.map { mapToEntity(it) }

}