package com.rbrauwers.investments.domain.usecase

import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.investments.domain.repository.TransactionsRepository
import com.rbrauwers.investments.util.ExtendedResult
import com.rbrauwers.investments.util.FlowableUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetTransactionsUseCase @Inject constructor(
    private val repository: TransactionsRepository
) : FlowableUseCase<GetTransactionsUseCase.Params, List<Transaction>>() {

    class Params

    override suspend fun invoke(params: Params): Flow<ExtendedResult<List<Transaction>>> = flow {
        emit(ExtendedResult.Loading)

        val result = ExtendedResult.from(
            runCatching {
                repository.getTransactions()
            }
        )

        emit(result)
    }

}