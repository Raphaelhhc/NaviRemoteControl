package com.example.nc_lab_qr_code_mobile.data

import android.util.Log
import com.example.nc_lab_qr_code_mobile.util.randomName
import com.google.firebase.Timestamp
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ListenerRegistration
import javax.inject.Inject

class SessionRepository @Inject constructor(
    private val sessionRef: CollectionReference
) {
    var session: Session? = null
    private var keywordListenerRegistration: ListenerRegistration? = null

    fun fetch(
        sessionId: String,
        onUpdated: (Boolean) -> Unit
    ) {
        sessionRef.document(sessionId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    println("DocumentSnapshot data: ${document.data}")
                    this.session = document.toObject(Session::class.java)
                    onUpdated(document.data != null)
                } else {
                    println("No such document")
                    onUpdated(false)
                }
            }
            .addOnFailureListener { e ->
                println("Failed to fetch session: $e")
                onUpdated(false)
            }
    }

    fun create(onCreated: (String?) -> Unit) {
        sessionRef.add(
            Session(
                sessionId = "",
                name = randomName(),
                keyword = "",
                createdAt = Timestamp.now(),
                updatedAt = Timestamp.now(),
            )
        )
            .addOnSuccessListener {
                println("Session successfully created! ${it.id}")
                onCreated(it.id)
            }
            .addOnFailureListener { e ->
                println("Failed to create session! $e")
                onCreated(null)
            }
    }

    fun delete(
        sessionId: String,
        onDeleted: (Boolean) -> Unit
    ) {
        sessionRef.document(sessionId).delete()
            .addOnSuccessListener {
                println("Session successfully deleted!")
                this.session = null
                onDeleted(true)
            }
            .addOnFailureListener { e ->
                println("Failed to delete session: $e")
                onDeleted(false)
            }
    }

    fun listen(
        sessionId: String,
        onKeyword: (String) -> Unit
    ) {
        keywordListenerRegistration = session?.let {
            println("listener start!")
            sessionRef.document(sessionId)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.e("Listener error", error.toString())
                        return@addSnapshotListener
                    }
                    val keyword = value?.getString("keyword")
                    if (keyword != null && keyword != it.keyword) {
                        println("Keyword updated: $keyword")
                        val updateSession = Session(
                            sessionId = session!!.sessionId,
                            name = session!!.name,
                            keyword = keyword,
                            createdAt = session!!.createdAt,
                            updatedAt = session!!.updatedAt
                        )
                        this.session = updateSession
                        onKeyword(keyword)
                    }
                }
        }
    }

    fun stopListen() {
        println("Listener Stop!")
        keywordListenerRegistration?.remove()
    }

    fun getKeyword(
        sessionId: String,
        onKeyword: (String) -> Unit
    ) {
        sessionRef.document(sessionId).get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val doc = document.toObject(Session::class.java)
                    val keyword = doc?.keyword
                    keyword?.let {
                        onKeyword(it)
                    }
                } else {
                    println("No such document")
                }
            }
            .addOnFailureListener { e ->
                println("get failed with: $e")
            }
    }

}