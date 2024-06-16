package com.atech.core.use_cases

import com.atech.core.model.TagModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

data class FirebaseDatabaseUseCases @Inject constructor(
    val getAllTags: GetAllUserCreatedTagsUseCase,
    val createNewTag: CreateNewTag,
    val deleteTag: DeleteTag
)


data class GetAllUserCreatedTagsUseCase @Inject constructor(
    private val db: FirebaseDatabase, private val auth: FirebaseAuth
) {
    operator fun invoke(
        onError: (String) -> Unit = {}, onSuccess: (List<Pair<TagModel, Boolean>>) -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return
        db.reference.child("tags").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                onSuccess(snapshot.children.mapNotNull {
                    it.getValue(TagModel::class.java)?.let { tagModel ->
                        tagModel to (tagModel.createdBy == uid)
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                onError(error.message)
            }

        })
    }
}

data class CreateNewTag @Inject constructor(
    private val db: FirebaseDatabase,
    private val auth: FirebaseAuth
) {
    operator fun invoke(
        tagModel: TagModel,
        onError: (String) -> Unit = {},
        onSuccess: () -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return
        db.reference.child("tags").child(tagModel.name.uppercase())
            .setValue(tagModel.copy(createdBy = uid))
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onError(it.exception?.message ?: "Something went wrong")
                }
            }

    }
}
data class DeleteTag @Inject constructor(
    private val db: FirebaseDatabase,
    private val auth: FirebaseAuth
) {
    operator fun invoke(
        tagModel: TagModel,
        onError: (String) -> Unit = {},
        onSuccess: () -> Unit
    ) {
        val uid = auth.currentUser?.uid ?: return
        db.reference.child("tags").child(tagModel.name.uppercase())
            .removeValue()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onError(it.exception?.message ?: "Something went wrong")
                }
            }

    }
}