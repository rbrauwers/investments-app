package com.rbrauwers.investments.di

import android.content.Context
import com.rbrauwers.csv.reader.domain.reader.CSVReader
import com.rbrauwers.investments.R
import com.rbrauwers.investments.data.TransactionsDefaultRepository
import com.rbrauwers.investments.domain.repository.TransactionsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class TransactionsModule {

    @Singleton
    @Binds
    abstract fun bindTransactionsRepository(
        repository: TransactionsDefaultRepository
    ): TransactionsRepository

}

@Module
@InstallIn(SingletonComponent::class)
internal object CSVReaderModule {

    @StatementReader
    @Provides
    fun provideStatementReader(@ApplicationContext context: Context): CSVReader {
        return CSVReader(context.resources.openRawResource(R.raw.statement))
    }

    @ExchangeReader
    @Provides
    fun provideExchangeReader(@ApplicationContext context: Context): CSVReader {
        return CSVReader(context.resources.openRawResource(R.raw.banking))
    }

}

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class StatementReader

@Qualifier
@Retention(AnnotationRetention.BINARY)
internal annotation class ExchangeReader