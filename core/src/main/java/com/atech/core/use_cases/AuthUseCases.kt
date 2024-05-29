package com.atech.core.use_cases

import com.atech.core.model.EducationDetails
import com.atech.core.model.ResearchPublishModel
import com.atech.core.model.StudentUserModel
import com.atech.core.model.UserType
import com.atech.core.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class AuthUseCases @Inject constructor(
    val logInWithGoogle: LogInWithGoogle,
    val isUserLoggedInUseCase: IsUserLoggedInUseCase,
    val getUserDetailsUseFromAuthCase: GetUserDetailsUseFromAuthCase,
    val saveDetails: SaveDetails,
    val publishApplication :PublishApplication,
    val signOut: SignOut
)

data class IsUserLoggedInUseCase @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke(): String? = auth.currentUser?.uid
}

data class GetUserDetailsUseFromAuthCase @Inject constructor(
    private val auth: FirebaseAuth,
    private val getUserUseCase: GetUserDataUseCase
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

data class LogInWithGoogle @Inject constructor(
    private val auth: FirebaseAuth,
    private val logInUseCase: LogInUseCase,
    private val hasUserData: HasUserUseCase,
) {
    suspend operator fun invoke(
        token: String,
        userType: UserType = UserType.STUDENTS,
        state: (State<String>) -> Unit = { _ -> }
    ) {
        try {
            val credential = GoogleAuthProvider.getCredential(token, null)
            val task = auth.signInWithCredential(credential).await()
            val user = task.user
            if (user == null) {
                state((State.Error(Exception("User not found"))))
            }
            user?.let { logInUser ->
                val userId = logInUser.uid
                val userName = logInUser.displayName
                val userEmail = logInUser.email
                val userPhoto = logInUser.photoUrl
                val studentUserModel = StudentUserModel(
                    uid = userId,
                    email = userEmail ?: "",
                    name = userName ?: "",
                    photoUrl = userPhoto?.toString() ?: "",
                    userType = userType.name
                )
                logInUseCase.invoke(userId, studentUserModel, state)
            }
        } catch (e: Exception) {
            state(State.Error(e))
        }
    }
}

data class SaveDetails @Inject constructor(
    private val auth: FirebaseAuth,
    private val saveData: SaveUserDetails
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
    private val auth: FirebaseAuth,
    private val publishApplication: PublishApplicationToDb
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
            model = model,
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