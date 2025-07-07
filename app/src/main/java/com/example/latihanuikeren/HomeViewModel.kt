package com.example.latihanuikeren

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore

class HomeViewModel : ViewModel(){
    val statusList = mutableStateOf(listOf<Status>())

    init {
        fetchStatuses()
    }

    private fun fetchStatuses(){
        val db = Firebase.firestore
        val currentUser = FirebaseAuth.getInstance().currentUser

        if (currentUser != null){
            db.collection("statuses").orderBy("timestamp",
                Query.Direction.DESCENDING).addSnapshotListener { snapshot, error ->
                    if (error != null){
                        return@addSnapshotListener
                    }
                    if (snapshot != null){
                        val statuses = snapshot.documents.mapNotNull { document ->
                            val statusData = document.toObject(Status::class.java)
                            statusData?.copy(id = document.id)
                        }
                        statusList.value = statuses
                    }
            }
        }
    }

    fun postStatus(statusText: String, onComplete: (Boolean) -> Unit){
        val db = Firebase.firestore
        val currentUser = FirebaseAuth.getInstance().currentUser?: return onComplete(false)

        val statusData = hashMapOf(
            "text" to statusText,
            "userId" to currentUser.uid,
            "userEmail" to currentUser.email,
            "timestamp" to FieldValue.serverTimestamp()
        )

        db.collection("statuses").add(statusData)
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun deleteStatus(statusId: String,  onComplete: (Boolean) -> Unit){
        val db = Firebase.firestore
        db.collection("statuses").document(statusId).delete()
            .addOnSuccessListener { onComplete(true) }
            .addOnFailureListener { onComplete(false) }
    }

    fun signOut(){
        FirebaseAuth.getInstance().signOut()
    }

    fun likeStatus(statusId: String){
        val db = Firebase.firestore
        val statusRef = db.collection("statuses").document(statusId)

        statusRef.update("likeCount", FieldValue.increment(1))
    }
}