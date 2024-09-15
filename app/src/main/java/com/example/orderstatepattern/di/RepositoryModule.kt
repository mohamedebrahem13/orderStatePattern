package com.example.orderstatepattern.di

import com.example.orderstatepattern.data.repository.CloudOrderRepository
import com.example.orderstatepattern.data.repository.LocalOrderRepository
import com.example.orderstatepattern.data.source.CloudOrderRepositoryImpl
import com.example.orderstatepattern.data.source.LocalOrderRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {


    @Singleton
    @Provides
    fun provideLocalOrderRepository(): LocalOrderRepository {
        return LocalOrderRepositoryImpl()
    }

    @Singleton
    @Provides
    fun provideCloudOrderRepository():CloudOrderRepository {
        return CloudOrderRepositoryImpl()
    }
}