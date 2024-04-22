package com.example.nc_lab_qr_code_mobile.di

import com.example.nc_lab_qr_code_mobile.util.Constants.SESSIONS
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    fun provideFirebaseFirestore() = FirebaseFirestore.getInstance()

    @Provides
    fun provideSessionRef(db: FirebaseFirestore) = db.collection(SESSIONS)
}