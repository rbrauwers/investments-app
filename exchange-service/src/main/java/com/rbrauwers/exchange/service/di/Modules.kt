package com.rbrauwers.exchange.service.di

import com.rbrauwers.exchange.service.data.repository.HistoricalExchangeRemoteRepositoryImpl
import com.rbrauwers.exchange.service.data.repository.HistoricalExchangeRepositoryImpl
import com.rbrauwers.exchange.service.domain.repository.HistoricalExchangeRemoteRepository
import com.rbrauwers.exchange.service.domain.repository.HistoricalExchangeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object HistoricalExchangeInternalModule {

    @Provides
    fun provideRemoteRepository(): HistoricalExchangeRemoteRepository {
        return HistoricalExchangeRemoteRepositoryImpl()
    }

}

@Module
@InstallIn(SingletonComponent::class)
internal abstract class HistoricalExchangeModule {

    @Singleton
    @Binds
    @Suppress("unused")
    abstract fun bindHistoricalExchangeRepository(
        repository: HistoricalExchangeRepositoryImpl
    ): HistoricalExchangeRepository

}