package com.rbrauwers.exchange.service.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.rbrauwers.exchange.service.domain.model.ExchangeRate
import com.rbrauwers.exchange.service.domain.repository.HistoricalExchangeLocalRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext

internal class HistoricalExchangeLocalRepositoryImpl(private val context: Context) : HistoricalExchangeLocalRepository {

    private val jsonAdapter: JsonAdapter<ExchangeRate> by lazy {
        Moshi.Builder().build().adapter(ExchangeRate::class.java)
    }

    override suspend fun getHistoricalExchange(date: String): ExchangeRate? {
        return withContext(Dispatchers.IO) {
            runCatching {
                context.exchangeDataStore.data.firstOrNull()?.get(getKey(date))?.let {
                    jsonAdapter.fromJson(it)
                }
            }.getOrNull()
        }
    }

    override suspend fun saveHistoricalExchange(date: String, rate: ExchangeRate?) {
        withContext(Dispatchers.IO) {
            context.exchangeDataStore.edit {
                it[getKey(date)] = jsonAdapter.toJson(rate)
            }
        }
    }

    private fun getKey(date: String): Preferences.Key<String> {
        return stringPreferencesKey(date)
    }

}

private val Context.exchangeDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "ExchangeDataStore"
)
