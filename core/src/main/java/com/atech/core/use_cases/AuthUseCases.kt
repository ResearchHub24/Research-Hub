package com.atech.core.use_cases

import com.atech.core.model.UserModel
import com.atech.core.model.UserType
import com.atech.core.utils.State
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class AuthUseCases @Inject constructor(
    val logInWithGoogle: LogInWithGoogle, val isUserLoggedInUseCase: IsUserLoggedInUseCase
)

data class IsUserLoggedInUseCase @Inject constructor(
    private val auth: FirebaseAuth
) {
    operator fun invoke(): String? = auth.currentUser?.uid

}

data class LogInWithGoogle @Inject constructor(
    private val auth: FirebaseAuth,
    private val logInUseCase: LogInUseCase,
    private val hasUserData: HasUserUseCase
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
                val userModel = UserModel(
                    uid = userId,
                    email = userEmail ?: "",
                    name = userName ?: "",
                    photoUrl = userPhoto?.toString() ?: "",
                    userType = userType.name
                )
                logInUseCase.invoke(userId, userModel, state)
            }
        } catch (e: Exception) {
            state(State.Error(e))
        }
    }
}
