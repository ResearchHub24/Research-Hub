package com.atech.core.use_cases

import com.atech.core.model.ResearchModel
import com.atech.core.room.ResearchHubDao
import com.atech.core.room.model.ResearchMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


data class WishListUseCases @Inject constructor(
    val getAll: GetAllUserCase,
    val getById: GetUserByIdUseCase,
    val deleteById: DeleteUserByIdUseCase,
    val insert: InsertUserUseCase,
    val deleteAll: DeleteAllUseCases,
    val isResearchExistUseCase: IsResearchExistUseCase,
    val deleteResearchNotInKeysUseCase: DeleteResearchNotInKeysUseCase,
    val getAllList: GetAllUserCaseList
)

data class GetAllUserCase @Inject constructor(
    private val dao: ResearchHubDao,
    private val mapper: ResearchMapper
) {
    operator fun invoke(): Flow<List<ResearchModel>> =
        dao.getAllResearch().map { mapper.mapToEntityList(it) }
}

data class GetUserByIdUseCase @Inject constructor(
    private val dao: ResearchHubDao,
    private val mapper: ResearchMapper
) {
    suspend operator fun invoke(id: String): ResearchModel? =
        dao.getResearchByKey(id)?.let { mapper.mapToEntity(it) }
}

data class DeleteUserByIdUseCase @Inject constructor(
    private val dao: ResearchHubDao
) {
    suspend operator fun invoke(id: String) = dao.deleteResearchByKey(id)
}

data class InsertUserUseCase @Inject constructor(
    private val dao: ResearchHubDao,
    private val mapper: ResearchMapper
) {
    suspend operator fun invoke(research: ResearchModel) =
        dao.insertResearch(mapper.mapFromEntity(research))
}

data class DeleteAllUseCases @Inject constructor(
    val dao: ResearchHubDao
) {
    suspend operator fun invoke() = dao.deleteAllResearch()
}

data class IsResearchExistUseCase @Inject constructor(
    private val dao: ResearchHubDao
) {
    suspend operator fun invoke(key: String) = dao.getResearchByKey(key) != null
}

data class DeleteResearchNotInKeysUseCase @Inject constructor(
    private val dao: ResearchHubDao
) {
    suspend operator fun invoke(keys: List<String>) = dao.deleteResearchNotInKeys(keys)
}

data class GetAllUserCaseList @Inject constructor(
    private val dao: ResearchHubDao,
    private val mapper: ResearchMapper
) {
    suspend operator fun invoke(): List<ResearchModel> =
        dao.getAllResearchList().map { mapper.mapToEntity(it) }
}