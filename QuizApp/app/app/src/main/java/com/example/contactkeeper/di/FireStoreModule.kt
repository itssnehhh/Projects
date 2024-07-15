package com.example.contactkeeper.di

import com.example.contactkeeper.data.firestore.FireStoreService
import com.example.contactkeeper.data.model.AppDispatcher
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FireStoreModule {

    @Provides
    @Singleton
    fun provideFirebaseFirestore(): FirebaseFirestore {
        return FirebaseFirestore.getInstance()
    }

    @Provides
    @Singleton
    fun provideAppDispatcher(): AppDispatcher {
        return AppDispatcher()
    }
}