package com.rbrauwers.investments.util

import kotlinx.coroutines.flow.Flow

abstract class UseCase<in Params, out Type : Any> {

    abstract suspend operator fun invoke(params: Params): ExtendedResult<Type>

}

abstract class FlowableUseCase<in Params, out Type : Any> {

    abstract suspend operator fun invoke(params: Params): Flow<ExtendedResult<Type>>

}