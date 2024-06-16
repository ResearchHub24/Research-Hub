package com.atech.core.use_cases

import com.atech.core.model.TagModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import javax.inject.Inject

data class FirebaseDatabaseUseCases @Inject constructor(
    val getAllTags: GetAllUserCreatedTagsUseCase
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