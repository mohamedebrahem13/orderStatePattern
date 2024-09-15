package com.example.orderstatepattern.di

import com.example.orderstatepattern.data.repository.CloudOrderRepository
import com.example.orderstatepattern.data.repository.LocalOrderRepository
import com.example.orderstatepattern.domain.usecase.ManageOrderUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Singleton
    @Provides
    fun provideManageOrderUseCase(
        localRepository: LocalOrderRepository,
        cloudRepository: CloudOrderRepository
    ): ManageOrderUseCase {
        return ManageOrderUseCase(localRepository, cloudRepository)
    }
}