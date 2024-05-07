package com.atech.core.use_cases

import com.atech.core.model.ResearchModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


data class ResearchUseCases @Inject constructor(
    val getAllResearchUseCase: GetAllResearchUseCase
)

class GetAllResearchUseCase @Inject constructor(
    private val firestore: FirebaseFirestore
) {
    operator fun invoke(): Flow<List<ResearchModel>> =
        firestore.collection("research")
            .snapshots()
            .map { it.toObjects(ResearchModel::class.java) }
}
