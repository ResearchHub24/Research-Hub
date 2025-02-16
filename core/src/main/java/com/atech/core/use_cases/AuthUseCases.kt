package com.atech.core.use_cases

import com.atech.core.model.EducationDetails
import com.atech.core.model.ResearchModel
import com.atech.core.model.ResearchPublishModel
import com.atech.core.model.StudentUserModel
import com.atech.core.model.TeacherUserModel
import com.atech.core.model.UserType
import com.atech.core.utils.DataState
import com.atech.core.utils.coreCheckIsAdmin
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class AuthUseCases @Inject constructor(
    val logInWithGoogleStudent: LogInWithGoogleStudent,
    val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    val getUserDetailsUseFromAuthCase: GetUserDetailsUseFromAuthCase,
    val saveDetails: SaveDetails,
    val publishApplication: PublishApplication,
    val signOut: SignOut,
)


data class IsUserLoggedInUseCase @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke(): String? = auth.currentUser?.uid
}

data class GetUserDetailsUseFromAuthCase @Inject constructor(
    private val auth: FirebaseAuth,
    private val getUserUseCase: GetStudentUserDataUseCase
) {
    suspend operator fun invoke(
        fromDatabase: Boolean = false
    ): StudentUserModel? = if (!fromDatabase && auth.currentUser != null) {
        val user = auth.currentUser
        StudentUserModel(
            uid = user!!.uid,
            email = user.email ?: "",
            name = user.displayName ?: "",
            photoUrl = user.photoUrl?.toString() ?: "",
            userType = ""
        )
    } else {
        getUserUseCase.invoke(auth.currentUser?.uid ?: "")
    }
}

data class LogInWithGoogleStudent @Inject constructor(
    private val auth: FirebaseAuth,
    private val logInUseCase: LogInUseCase,
    private val setTokenUseCase: SetTokenUseCase
) {
    suspend operator fun invoke(
        uid: String,
        state: (DataState<String>) -> Unit = { _ -> }
    ) {
        try {
            val credential = GoogleAuthProvider.getCredential(uid, null)
            val task = auth.signInWithCredential(credential).await()
            val user = task.user
            if (user == null) {
                state((DataState.Error(Exception("User not found"))))
            }
            user?.let { logInUser ->
                val userId = logInUser.uid
                val userName = logInUser.displayName
                val userEmail = logInUser.email
                val userPhoto = logInUser.photoUrl
                val studentUserModel = coreCheckIsAdmin {
                    TeacherUserModel(
                        uid = userId,
                        email = userEmail ?: "",
                        name = userName ?: "",
                        photoUrl = userPhoto?.toString() ?: "",
                        userType = UserType.PROFESSORS.name,
                        verified = false,
                    )
                } ?: StudentUserModel(
                    uid = userId,
                    email = userEmail ?: "",
                    name = userName ?: "",
                    photoUrl = userPhoto?.toString() ?: "",
                    userType = UserType.STUDENTS.name,
                )
                logInUseCase.invoke(userId, studentUserModel) { state1 ->
                    if (state1 is DataState.Error)
                        state(state1)
                    if (state1 is DataState.Success) {
                        runBlocking {
                            setTokenUseCase.invoke(
                                uid = state1.data
                            ) { error ->
                                if (error != null)
                                    state.invoke(DataState.Error(error))
                                else
                                    state.invoke(state1)
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            state(DataState.Error(e))
        }
    }
}

data class SaveDetails @Inject constructor(
    private val auth: FirebaseAuth, private val saveData: SaveStudentUserDetails
) {
    suspend fun saveProfileData(
        name: String, phone: String, onComplete: (Exception?) -> Unit = {}
    ) = saveData.saveProfileData(auth.currentUser?.uid ?: "", name, phone, onComplete)

    suspend fun saveEducationData(
        educationDetails: List<EducationDetails>, onComplete: (Exception?) -> Unit = {}
    ) = saveData.saveEducationData(auth.currentUser?.uid ?: "", educationDetails, onComplete)

    suspend fun saveSkillData(
        skillList: List<String>, onComplete: (Exception?) -> Unit = {}
    ) = saveData.saveSkillData(auth.currentUser?.uid ?: "", skillList, onComplete)

}

data class PublishApplication @Inject constructor(
    private val auth: FirebaseAuth, private val publishApplication: PublishApplicationToDb
) {
    suspend operator fun invoke(
        key: String,
        filledForm: String,
        model: ResearchPublishModel,
        onComplete: (Exception?) -> Unit = {}
    ) {
        publishApplication.invoke(
            uid = auth.currentUser?.uid ?: "",
            key = key,
            model = model.copy(
                uid = auth.currentUser?.uid,
                profileImg = auth.currentUser?.photoUrl?.toString() ?: ""
            ),
            filledForm = filledForm,
            onComplete = onComplete
        )
    }
}

data class SignOut @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke(
        action: () -> Unit
    ) {
        auth.signOut()
        action.invoke()
    }
}

// --------------------------------------------- Teacher -------------------------------------------------------------

data class TeacherAuthUserCase @Inject constructor(
    val getTeacherData: GetTeacherData,
    val saveData: SaveTeacherData,
    val getAllPosted: GetAllPosted,
    val saveResearch: SaveResearch,
    val getAllSubmittedForm: GetAllSubmittedForm
)

data class GetAllPosted @Inject constructor(
    private val auth: FirebaseAuth, private val getAllPostedResearch: GetAllPostedResearch
) {
    operator fun invoke(): Flow<List<ResearchModel>> =
        getAllPostedResearch.invoke(auth.currentUser?.uid ?: "")
}

data class GetTeacherData @Inject constructor(
    private val auth: FirebaseAuth, private val getTeacherData: GetTeacherUserDataUseCase
) {
    suspend operator fun invoke(): TeacherUserModel? =
        getTeacherData.invoke(auth.currentUser?.uid ?: "")
}

data class SaveTeacherData @Inject constructor(
    private val auth: FirebaseAuth, private val saveData: SaveTeacherUserData
) {
    suspend fun savePassword(
        password: String, onComplete: (Exception?) -> Unit = {}
    ) {
        saveData.savePassword(auth.currentUser?.uid ?: "", password, onComplete)
    }
}

data class SaveResearch @Inject constructor(
    private val auth: FirebaseAuth, private val saveResearchData: SaveResearchData
) {
    operator fun invoke(
        model: ResearchModel, onComplete: (Exception?) -> Unit
    ) {
        try {
            saveResearchData.invoke(
                uid = auth.currentUser?.uid ?: return,
                name = auth.currentUser?.displayName ?: "User ${auth.currentUser?.uid?.take(4)}",
                model = model,
                onComplete = onComplete
            )
        } catch (e: Exception) {
            onComplete.invoke(e)
        }
    }
}