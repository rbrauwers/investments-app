package com.rbrauwers.csv.reader.di

import android.content.Context
import com.rbrauwers.csv.reader.R
import com.rbrauwers.csv.reader.domain.reader.BankingReader
import com.rbrauwers.csv.reader.domain.reader.CSVReader
import com.rbrauwers.csv.reader.domain.reader.ForexReader
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
internal object CSVReaderModule {

    @StatementReader
    @Provides
    fun provideStatementReader(@ApplicationContext context: Context): CSVReader {
        return BankingReader(context.resources.openRawResource(R.raw.statement))
    }

    @ExchangeReader
    @Provides
    fun provideExchangeReader(@ApplicationContext context: Context): CSVReader {
        return BankingReader(context.resources.openRawResource(R.raw.banking))
    }

    @Provides
    fun provideForexReader(@ApplicationContext context: Context): CSVReader {
        return ForexReader(context.resources.openRawResource(R.raw.report_forex))
    }

}

/**
 * Must be public
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class StatementReader

/**
 * Must be public
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ExchangeReader