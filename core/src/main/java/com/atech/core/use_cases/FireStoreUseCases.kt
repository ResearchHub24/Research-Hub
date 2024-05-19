package com.atech.core.use_cases

import android.util.Log
import com.atech.core.model.ResearchModel
import com.atech.core.model.UserModel
import com.atech.core.utils.AppErrors
import com.atech.core.utils.CollectionName
import com.atech.core.utils.State
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class FireStoreUseCases @Inject constructor(
    val getAllResearchUseCase: GetAllResearchUseCase
)

class GetAllResearchUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(): Flow<List<ResearchModel>> =
        db.collection("research")
            .snapshots()
            .map { it.toObjects(ResearchModel::class.java) }
}

data class LogInUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val hasUserUseCase: HasUserUseCase
) {
    suspend operator fun invoke(
        uid: String,
        model: UserModel,
        state: (State<String>) -> Unit = { _ -> }
    ) {
        try {
            hasUserUseCase.invoke(uid) { state1 ->
                when (state1) {
                    is State.Error -> {
                        state(state1)
                    }

                    is State.Success -> {
                        if (!state1.data) {
                            runBlocking {
                                db.collection(CollectionName.USER.value)
                                    .document(uid)
                                    .set(model)
                                    .await()
                                state(State.Success(uid))
                            }
                        } else {
                            state(State.Success(uid))
                        }
                    }

                    else -> {}
                }
            }
        } catch (e: Exception) {
            state(State.Error(e))
        }
    }
}

data class HasUserUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend operator fun invoke(
        uid: String,
        state: (State<Boolean>) -> Unit = { _ -> }
    ) {
        try {
            val doc = db.collection(CollectionName.USER.value)
                .document(uid)
                .get()
                .await()
            state(State.Success(doc.exists()))
        } catch (e: Exception) {
            state(State.Error(e))
        }
    }
}

data class GetUserDataUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend operator fun invoke(uid: String): UserModel? =
        try {
            db.collection(CollectionName.USER.value)
                .document(uid)
                .get()
                .await()
                .toObject(UserModel::class.java)
        } catch (e: Exception) {
            Log.e(AppErrors.ERROR.name, "invoke: $e")
            null
        }
}