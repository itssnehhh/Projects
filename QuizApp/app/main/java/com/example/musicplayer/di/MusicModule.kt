package com.example.musicplayer.di

import com.example.musicplayer.data.service.MusicService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.greenrobot.eventbus.EventBus
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicModule {

    @Provides
    @Singleton
    fun musicService() = MusicService()
}