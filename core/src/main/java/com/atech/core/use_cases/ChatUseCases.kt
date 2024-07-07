package com.atech.core.use_cases

import android.util.Log
import com.atech.core.model.AllChatModel
import com.atech.core.utils.CollectionName
import com.atech.core.utils.TAGS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class ChatUseCases @Inject constructor(
    val createChat: CreateChatUseCase
)

private fun getRootPath(
    senderUid: String,
    senderName: String,
    receiverName: String,
    receiverUid: String,
) = "${senderUid}_${senderName}_${receiverUid}_${receiverName}"


data class CreateChatUseCase @Inject constructor(
    private val db: FirebaseFirestore, private val auth: FirebaseAuth
) {
    suspend operator fun invoke(
        receiverName: String,
        receiverUid: String,
        receiverProfileUrl: String,
        onComplete: (Exception?) -> Unit
    ) {
        val senderUid = auth.currentUser?.uid ?: return
        val senderName = auth.currentUser?.displayName ?: return
        val senderProfileUrl = auth.currentUser?.photoUrl.toString()
        val isDataExists = checkIfChatExists(
            senderUid = senderUid,
            senderName = senderName,
            receiverName = receiverName,
            receiverUid = receiverUid
        )
        if (isDataExists) {
            onComplete.invoke(null)
            return
        }
        try {
            val path = getRootPath(
                senderUid, senderName, receiverName, receiverUid
            )
            db.collection(
                CollectionName.CHATS.value
            ).document(
                path
            ).set(
                AllChatModel(
                    senderUid = senderUid,
                    senderName = senderName,
                    receiverName = receiverName,
                    receiverUid = receiverUid,
                    senderProfileUrl = senderProfileUrl,
                    receiverProfileUrl = receiverProfileUrl,
                    path = path
                )
            ).await()
            onComplete.invoke(null)
        } catch (e: Exception) {
            onComplete.invoke(e)
        }
    }

    private suspend fun checkIfChatExists(
        senderUid: String,
        senderName: String,
        receiverName: String,
        receiverUid: String,
    ): Boolean = try {
        val doc = db.collection(
            CollectionName.CHATS.value
        ).document(
            getRootPath(
                senderUid, senderName, receiverName, receiverUid
            )
        ).get().await()
        doc.exists()
    } catch (e: Exception) {
        Log.e(TAGS.ERROR.name, "checkIfChatExists: $e")
        false
    }

}