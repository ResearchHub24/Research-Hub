package com.atech.core.use_cases

import android.util.Log
import com.atech.core.model.AllChatModel
import com.atech.core.model.MessageModel
import com.atech.core.utils.CollectionName
import com.atech.core.utils.TAGS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.snapshots
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


data class ChatUseCases @Inject constructor(
    val createChat: CreateChatUseCase,
    val getAllChats: GetAllChatsUseCase,
    val sendMessage: SendMessageUseCases,
    val getAllMessage: GetMessageUseCases,
    val deleteMessage: DeleteMessageUseCase
)

private fun getRootPath(
    senderUid: String,
    senderName: String,
    receiverName: String,
    receiverUid: String,
) = "${senderUid}_${senderName}_${receiverUid}_${receiverName}"


data class CreateChatUseCase @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val sendMessage: SendMessageUseCases
) {
    suspend operator fun invoke(
        receiverName: String,
        receiverUid: String,
        receiverProfileUrl: String,
        message: String,
        onComplete: (Exception?) -> Unit
    ) {
        val senderUid = auth.currentUser?.uid ?: return
        val senderName = auth.currentUser?.displayName ?: return
        val senderProfileUrl = auth.currentUser?.photoUrl.toString()
        val path = getRootPath(
            senderUid, senderName, receiverName, receiverUid
        )
        val isDataExists = checkIfChatExists(
            senderUid = senderUid,
            senderName = senderName,
            receiverName = receiverName,
            receiverUid = receiverUid
        )
        if (isDataExists) {
            sendMessage.invoke(
                receiverName = receiverName,
                receiverUid = receiverUid,
                message = message,
                onComplete = onComplete,
                path = path
            )
            return
        }
        try {
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
            sendMessage.invoke(
                receiverName = receiverName,
                receiverUid = receiverUid,
                message = message,
                onComplete = onComplete,
                path = path,
            )
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

data class GetAllChatsUseCase @Inject constructor(
    private val db: FirebaseFirestore, private val auth: FirebaseAuth
) {
    operator fun invoke(
        forAdmin: Boolean = true,
        onError: (Exception) -> Unit = {},
    ): Flow<List<AllChatModel>> = try {
        val senderUid = auth.currentUser?.uid!!
        val allChats =
            db.collection(CollectionName.CHATS.value)
                .whereEqualTo(if (forAdmin) "senderUid" else "receiverUid", senderUid)
                .snapshots()
                .map { it.toObjects(AllChatModel::class.java) }
        allChats
    } catch (e: Exception) {
        onError.invoke(e)
        emptyFlow()
    }

}

data class SendMessageUseCases @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    suspend operator fun invoke(
        receiverName: String,
        receiverUid: String,
        message: String,
        path: String,
        onComplete: (Exception?) -> Unit
    ) {
        val senderUid = auth.currentUser?.uid ?: return
        val senderName = auth.currentUser?.displayName ?: return
        try {
            val ref = db.collection(CollectionName.CHATS.value)
                .document(path)
                .collection(CollectionName.MESSAGES.value)
            val doc = ref.document().id
            ref.document(doc)
                .set(
                    MessageModel(
                        senderUid = senderUid,
                        senderName = senderName,
                        receiverUid = receiverUid,
                        receiverName = receiverName,
                        message = message,
                        path = doc
                    )
                )
                .await()
            onComplete.invoke(null)
        } catch (e: Exception) {
            onComplete.invoke(e)
        }
    }
}

data class GetMessageUseCases @Inject constructor(
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
) {
    operator fun invoke(
        path: String,
    ) =
        try {
            db.collection(CollectionName.CHATS.value)
                .document(path)
                .collection(CollectionName.MESSAGES.value)
                .snapshots()
                .map {
                    it.toObjects(MessageModel::class.java)
                }
        } catch (e: Exception) {
            Log.e(TAGS.ERROR.name, "invoke: $e")
            emptyFlow()
        }
}

data class DeleteMessageUseCase @Inject constructor(
    private val db: FirebaseFirestore
) {
    suspend operator fun invoke(
        rootPath: String,
        docPath: String,
        onComplete: (Exception?) -> Unit
    ) {
        try {
            db.collection(CollectionName.CHATS.value)
                .document(rootPath)
                .collection(CollectionName.MESSAGES.value)
                .document(docPath)
                .delete()
                .await()
            onComplete.invoke(null)
        } catch (e: Exception) {
            Log.e(TAGS.ERROR.name, "invoke: $e")
            onComplete.invoke(e)
        }
    }
}