package com.rbrauwers.investments.di

import com.rbrauwers.investments.data.TransactionsDefaultRepository
import com.rbrauwers.investments.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TransactionsModule {

    @Singleton
    @Binds
    @Suppress("unused")
    abstract fun bindTransactionsRepository(
        repository: TransactionsDefaultRepository
    ): TransactionsRepository

}