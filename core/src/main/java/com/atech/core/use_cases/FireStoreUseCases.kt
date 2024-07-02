package com.atech.core.use_cases

import android.util.Log
import com.atech.core.model.EducationDetails
import com.atech.core.model.ResearchModel
import com.atech.core.model.ResearchPublishModel
import com.atech.core.model.StudentUserModel
import com.atech.core.model.TeacherUserModel
import com.atech.core.model.UserModel
import com.atech.core.model.UserType
import com.atech.core.utils.CollectionName
import com.atech.core.utils.State
import com.atech.core.utils.TAGS
import com.atech.core.utils.coreCheckIsAdmin
import com.atech.core.utils.fromJsonList
import com.atech.core.utils.toJSON
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
    operator fun invoke(
        isEmpty: (Boolean) -> Unit
    ): Flow<List<ResearchModel>> =
        db.collection("research").snapshots().map {
            val researchModels = it.toObjects(ResearchModel::class.java)
            isEmpty.invoke(researchModels.isEmpty())
            researchModels
        }
}

data class LogInUseCase @Inject constructor(
    private val db: FirebaseFirestore, private val hasUserUseCase: HasUserUseCase
) {
    suspend operator fun <T : UserModel> invoke(
        uid: String,
        model: T,
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
                                db.collection(CollectionName.USER.value).document(uid).set(model)
                                    .await()
                                state(State.Success(uid))
                            }
                        } else {
                            coreCheckIsAdmin {
                                runBlocking {
                                    db.collection(CollectionName.USER.value).document(uid).get()
                                        .await().toObject(TeacherUserModel::class.java)
                                        ?.let { userModel ->
                                            if (userModel.userType != UserType.PROFESSORS.name)
                                                state.invoke(
                                                    State.Error(Exception("This mail is linked with student account & can't be used for faculty account"))
                                                )
                                            else
                                                state(State.Success(uid))
                                        }
                                }
                            } ?: state(State.Success(uid))
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
        uid: String, state: (State<Boolean>) -> Unit = { _ -> }
    ) {
        try {
            val doc = db.collection(CollectionName.USER.value).document(uid).get().await()
            state(State.Success(doc.exists()))
        } catch (e: Exception) {
            state(State.Error(e))
        }
    }
}

data class GetStudentUserDataUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend operator fun invoke(uid: String): StudentUserModel? = try {
        db.collection(CollectionName.USER.value).document(uid).get().await()
            .toObject(StudentUserModel::class.java)
    } catch (e: Exception) {
        Log.e(TAGS.ERROR.name, "invoke: $e")
        null
    }
}

data class GetTeacherUserDataUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend operator fun invoke(uid: String): TeacherUserModel? = try {
        db.collection(CollectionName.USER.value).document(uid).get().await()
            .toObject(TeacherUserModel::class.java)
    } catch (e: Exception) {
        Log.e(TAGS.ERROR.name, "invoke: $e")
        null
    }
}


data class SaveTeacherUserData @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun savePassword(
        uid: String,
        password: String,
        onComplete: (Exception?) -> Unit = {}
    ) {
        try {
            db.collection(
                CollectionName.USER.value
            ).document(uid)
                .update(
                    mapOf(
                        "password" to password
                    )
                ).await()
            onComplete(null)
        } catch (e: Exception) {
            Log.e(TAGS.ERROR.name, "savePassword: $e")
            onComplete(e)
        }
    }
}


data class SaveStudentUserDetails @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend fun saveProfileData(
        uid: String, name: String, phone: String, onComplete: (Exception?) -> Unit = {}
    ) {
        try {
            db.collection(CollectionName.USER.value).document(uid).update(
                mapOf(
                    "name" to name, "phone" to phone
                )
            ).await()
            onComplete(null)
        } catch (e: Exception) {
            Log.e(TAGS.ERROR.name, "saveProfileData: $e")
            onComplete(e)
        }
    }

    suspend fun saveEducationData(
        uid: String, models: List<EducationDetails>, onComplete: (Exception?) -> Unit = {}
    ) {
        try {
            db.collection(CollectionName.USER.value).document(uid).update(
                mapOf(
                    "educationDetails" to toJSON(models)
                )
            ).await()
            onComplete(null)
        } catch (e: Exception) {
            Log.e(TAGS.ERROR.name, "saveEducationData: $e")
            onComplete(e)
        }
    }

    suspend fun saveSkillData(
        uid: String,
        models: List<String>,
        onComplete: (Exception?) -> Unit = {}
    ) {
        try {
            db.collection(CollectionName.USER.value).document(uid).update(
                mapOf(
                    "skillList" to toJSON(models)
                )
            ).await()
            onComplete(null)
        } catch (e: Exception) {
            Log.e(TAGS.ERROR.name, "saveEducationData: $e")
            onComplete(e)
        }
    }

    suspend fun saveFilledForm(
        uid: String,
        filledForm: String,
        key: String,
        onComplete: (Exception?) -> Unit = {}
    ) {
        try {

            val list = fromJsonList<String>(filledForm).toMutableList()
            list.add(key)
            db.collection(CollectionName.USER.value).document(uid).update(
                mapOf(
                    "filledForm" to toJSON(list)
                )
            ).await()
            onComplete(null)
        } catch (e: Exception) {
            Log.e(TAGS.ERROR.name, "saveEducationData: $e")
            onComplete(e)
        }
    }
}


data class PublishApplicationToDb @Inject constructor(
    private val db: FirebaseFirestore,
    private val saveStudentUserDetails: SaveStudentUserDetails
) {
    suspend operator fun invoke(
        uid: String,
        key: String,
        filledForm: String,
        model: ResearchPublishModel,
        onComplete: (Exception?) -> Unit = {}
    ) {
        try {
            db.collection(CollectionName.RESEARCH.value)
                .document(key)
                .collection(CollectionName.SUBMITTED_FORM.value)
                .document(uid)
                .set(model)
                .await()
            saveStudentUserDetails.saveFilledForm(
                uid = uid,
                key = key,
                filledForm = filledForm,
                onComplete = onComplete
            )
            onComplete.invoke(null)
        } catch (e: Exception) {
            Log.e(TAGS.ERROR.name, "saveEducationData: $e")
            onComplete(e)
        }
    }
}

data class GetAllFaculties @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(): Flow<List<TeacherUserModel>> =
        db.collection(CollectionName.USER.value)
            .whereEqualTo("userType", UserType.PROFESSORS.name)
            .whereEqualTo("verified", true)
            .snapshots()
            .map { it.toObjects(TeacherUserModel::class.java) }

}

data class GetAllPostedResearch @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(ui: String): Flow<List<ResearchModel>> =
        db.collection(CollectionName.RESEARCH.value)
            .whereEqualTo("created_by_UID", ui)
            .snapshots()
            .map { it.toObjects(ResearchModel::class.java) }
}

data class SaveResearchData @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(
        uid: String,
        name: String,
        model: ResearchModel,
        onComplete: (Exception?) -> Unit
    ) {
        val ref = db.collection(CollectionName.RESEARCH.value)
        val modelWithKey = (if (model.key == null)
            model.copy(
                key = ref.document().id
            )
        else model).copy(
            createdByUID = uid,
            createdBy = name
        )
        requireNotNull(modelWithKey.key) {
            "Key can't be Null"
        }
        ref.document(modelWithKey.key)
            .set(modelWithKey)
            .addOnCompleteListener {
                onComplete.invoke(null)
            }
            .addOnFailureListener {
                onComplete.invoke(it)
            }
    }
}

data class GetAllSubmittedForm @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(
        key: String
    ): Flow<List<ResearchPublishModel>> =
        db.collection(CollectionName.RESEARCH.value)
            .document(key)
            .collection(CollectionName.SUBMITTED_FORM.value)
            .snapshots()
            .map { it.toObjects(ResearchPublishModel::class.java) }

}