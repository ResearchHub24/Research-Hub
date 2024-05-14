package com.atech.core.room.model

import androidx.annotation.Keep
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.atech.core.model.ResearchModel
import com.atech.core.utils.EntityMapper
import javax.inject.Inject

@Keep
@Entity(tableName = "research_table")
data class ResearchRoomModel(
    val title: String,
    val description: String,
    val createdBy: String,
    val createdByUID: String,
    val created: Long,
    val deadLine: Long?,
    val tags: String,
    val addedAt: Long = System.currentTimeMillis(),
    @PrimaryKey(autoGenerate = false)
    val key: String = ""
)

class ResearchMapper @Inject constructor() : EntityMapper<ResearchModel, ResearchRoomModel> {
    override fun mapFromEntity(entity: ResearchModel): ResearchRoomModel {
        return ResearchRoomModel(
            title = entity.title ?: "No Title",
            description = entity.description ?: "No Description",
            createdBy = entity.createdBy ?: "No Created By",
            createdByUID = entity.createdByUID ?: "No Created By UID",
            created = entity.created ?: 0L,
            deadLine = entity.deadLine,
            tags = entity.tags ?: "",
            key = entity.key ?: ""
        )
    }

    override fun mapToEntity(domainModel: ResearchRoomModel): ResearchModel {
        return ResearchModel(
            title = domainModel.title,
            description = domainModel.description,
            createdBy = domainModel.createdBy,
            createdByUID = domainModel.createdByUID,
            created = domainModel.created,
            deadLine = domainModel.deadLine,
            tags = domainModel.tags,
            key = domainModel.key
        )
    }

    fun mapFromEntityList(entities: List<ResearchModel>): List<ResearchRoomModel> {
        return entities.map { mapFromEntity(it) }
    }
    fun mapToEntityList(entities: List<ResearchRoomModel>): List<ResearchModel> {
        return entities.map { mapToEntity(it) }
    }
}