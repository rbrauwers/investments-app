package com.rbrauwers.investments.domain.usecase

import com.rbrauwers.investments.domain.model.TransactionsGroup
import com.rbrauwers.investments.domain.repository.TransactionsRepository
import com.rbrauwers.investments.util.ExtendedResult
import com.rbrauwers.investments.util.FlowableUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class GetTransactionsGroupsUseCase @Inject constructor(
    private val repository: TransactionsRepository
) : FlowableUseCase<GetTransactionsGroupsUseCase.Params, Set<TransactionsGroup>>() {

    class Params

    override suspend fun invoke(params: Params): Flow<ExtendedResult<Set<TransactionsGroup>>> = flow {
        emit(ExtendedResult.Loading)

        val result = ExtendedResult.from(
            runCatching {
                repository.getTransactionsGroups()
            }
        )

        emit(result)
    }

}