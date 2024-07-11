package com.atech.core.use_cases

import android.content.SharedPreferences
import android.util.Log
import com.atech.core.model.Action
import com.atech.core.model.EducationDetails
import com.atech.core.model.ResearchModel
import com.atech.core.model.ResearchPublishModel
import com.atech.core.model.StudentUserModel
import com.atech.core.model.TeacherUserModel
import com.atech.core.model.UserModel
import com.atech.core.model.UserType
import com.atech.core.utils.CollectionName
import com.atech.core.utils.PrefKeys
import com.atech.core.utils.DataState
import com.atech.core.utils.TAGS
import com.atech.core.utils.coreCheckIsAdmin
import com.atech.core.utils.fromJsonList
import com.atech.core.utils.toJSON
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import com.google.firebase.messaging.FirebaseMessaging
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
    ): Flow<List<ResearchModel>> = db.collection("research").snapshots().map {
        val researchModels = it.toObjects(ResearchModel::class.java)
        isEmpty.invoke(researchModels.isEmpty())
        researchModels
    }
}

data class LogInUseCase @Inject constructor(
    private val db: FirebaseFirestore, private val hasUserUseCase: HasUserUseCase
) {
    suspend operator fun <T : UserModel> invoke(
        uid: String, model: T, state: (DataState<String>) -> Unit = { _ -> }
    ) {
        try {
            hasUserUseCase.invoke(uid) { state1 ->
                when (state1) {
                    is DataState.Error -> {
                        state(state1)
                    }

                    is DataState.Success -> {
                        if (!state1.data) {
                            runBlocking {
                                db.collection(CollectionName.USER.value).document(uid).set(model)
                                    .await()
                                state(DataState.Success(uid))
                            }
                        } else {
                            coreCheckIsAdmin {
                                runBlocking {
                                    db.collection(CollectionName.USER.value).document(uid).get()
                                        .await().toObject(TeacherUserModel::class.java)
                                        ?.let { userModel ->
                                            if (userModel.userType != UserType.PROFESSORS.name) state.invoke(
                                                DataState.Error(Exception("This mail is linked with student account & can't be used for faculty account"))
                                            )
                                            else state(DataState.Success(uid))
                                        }
                                }
                            } ?: state(DataState.Success(uid))
                        }
                    }

                    else -> {}
                }
            }
        } catch (e: Exception) {
            state(DataState.Error(e))
        }
    }
}

data class HasUserUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend operator fun invoke(
        uid: String, state: (DataState<Boolean>) -> Unit = { _ -> }
    ) {
        try {
            val doc = db.collection(CollectionName.USER.value).document(uid).get().await()
            state(DataState.Success(doc.exists()))
        } catch (e: Exception) {
            state(DataState.Error(e))
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
        uid: String, password: String, onComplete: (Exception?) -> Unit = {}
    ) {
        try {
            db.collection(
                CollectionName.USER.value
            ).document(uid).update(
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
        uid: String, models: List<String>, onComplete: (Exception?) -> Unit = {}
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
        uid: String, filledForm: String, key: String, onComplete: (Exception?) -> Unit = {}
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
    private val db: FirebaseFirestore, private val saveStudentUserDetails: SaveStudentUserDetails
) {
    suspend operator fun invoke(
        uid: String,
        key: String,
        filledForm: String,
        model: ResearchPublishModel,
        onComplete: (Exception?) -> Unit = {}
    ) {
        try {
            db.collection(CollectionName.RESEARCH.value).document(key)
                .collection(CollectionName.SUBMITTED_FORM.value).document(uid).set(model).await()
            saveStudentUserDetails.saveFilledForm(
                uid = uid, key = key, filledForm = filledForm, onComplete = onComplete
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
        db.collection(CollectionName.USER.value).whereEqualTo("userType", UserType.PROFESSORS.name)
            .whereEqualTo("verified", true).snapshots()
            .map { it.toObjects(TeacherUserModel::class.java) }

}

data class GetAllPostedResearch @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(ui: String): Flow<List<ResearchModel>> =
        db.collection(CollectionName.RESEARCH.value).whereEqualTo("created_by_UID", ui).snapshots()
            .map { it.toObjects(ResearchModel::class.java) }
}

data class SaveResearchData @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(
        uid: String, name: String, model: ResearchModel, onComplete: (Exception?) -> Unit
    ) {
        val ref = db.collection(CollectionName.RESEARCH.value)
        val modelWithKey = (if (model.key == null) model.copy(
            key = ref.document().id
        )
        else model).copy(
            createdByUID = uid, createdBy = name
        )
        requireNotNull(modelWithKey.key) {
            "Key can't be Null"
        }
        ref.document(modelWithKey.key).set(modelWithKey).addOnCompleteListener {
            onComplete.invoke(null)
        }.addOnFailureListener {
            onComplete.invoke(it)
        }
    }
}

data class GetAllSubmittedForm @Inject constructor(
    private val db: FirebaseFirestore
) {
    operator fun invoke(
        key: String
    ): Flow<List<ResearchPublishModel>> = db.collection(CollectionName.RESEARCH.value).document(key)
        .collection(CollectionName.SUBMITTED_FORM.value).snapshots()
        .map { it.toObjects(ResearchPublishModel::class.java) }

}

data class SelectUseUserCase @Inject constructor(
    private val db: FirebaseFirestore,
) {
    operator fun invoke(
        key: String,
        uid: String,
        action: Action = Action.UNSELECTED,
        onComplete: (Exception?) -> Unit
    ) {
        val researchDbReference = db.collection(CollectionName.RESEARCH.value).document(key)
        val userDbReference = db.collection(CollectionName.USER.value).document(uid)
        researchDbReference.get().addOnSuccessListener { document ->
            researchDbReference.update(
                mapOf(
                    "selected_users" to toJSON(fromJsonList<String>(
                        document.getString("selected_users") ?: ""
                    ).toMutableList().apply {
                        when (action) {
                            Action.SELECTED -> add(uid)
                            Action.UNSELECTED -> remove(uid)
                        }
                    })
                )
            ).addOnSuccessListener { // Level 2
                userDbReference.get().addOnSuccessListener { document ->
                    userDbReference.update(
                        mapOf(
                            "selectedForm" to toJSON(fromJsonList<String>(
                                document.getString("selectedForm") ?: ""
                            ).toMutableList().apply {
                                when (action) {
                                    Action.SELECTED -> add(key)
                                    Action.UNSELECTED -> remove(key)
                                }
                            })
                        )
                    ).addOnSuccessListener {
                        onComplete.invoke(null)
                    }.addOnFailureListener(onComplete)
                }
            }.addOnFailureListener(onComplete)
        }.addOnFailureListener(onComplete)

    }
}

data class SetTokenUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val messaging: FirebaseMessaging,
    private val pref: SharedPreferences
) {
    suspend operator fun invoke(
        uid: String, localToken: String? = null, onComplete: (Exception?) -> Unit
    ) {
        try {
            val token = localToken ?: messaging.token.await()
            pref.edit().putString(PrefKeys.DEVICE_TOKEN.value, token).apply()
            val deviceToken = hashMapOf(
                "token" to token,
                "timestamp" to FieldValue.serverTimestamp(),
            )
            db.collection(CollectionName.FCM_TOKEN.value).document(uid).set(deviceToken).await()
            onComplete(null)
        } catch (e: Exception) {
            onComplete(e)
        }
    }
}

class NoUserLogInException(override val message: String) : Exception(message)

data class SetTokenWorkManagerUseCase @Inject constructor(
    private val auth: IsUserLoggedInUseCase,
    private val db: FirebaseFirestore,
    private val messaging: FirebaseMessaging,
    private val pref: SharedPreferences
) {
    suspend operator fun <T> invoke(
        onComplete: (Exception?) -> T
    ): T = try {
        val uid = auth.invoke()
        if (uid == null) {
            onComplete.invoke(NoUserLogInException("User is"))
        } else {
            val token = messaging.token.await()
            pref.edit().putString(PrefKeys.DEVICE_TOKEN.value, token).apply()
            val deviceToken = hashMapOf(
                "token" to token,
                "timestamp" to FieldValue.serverTimestamp(),
            )
            db.collection(CollectionName.FCM_TOKEN.value).document(uid).set(deviceToken).await()
            onComplete(null)
        }
    } catch (e: Exception) {
        onComplete(e)
    }
}