package com.example.nc_lab_qr_code_mobile.data

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

data class Session(
    @DocumentId val sessionId: String = "",
    val name : String = "",
    val keyword : String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null,
)
