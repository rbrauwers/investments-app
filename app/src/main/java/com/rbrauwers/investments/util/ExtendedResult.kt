package com.rbrauwers.investments.util

sealed class ExtendedResult<out T> {
    data class Success<out T>(val value: T) : ExtendedResult<T>()
    data class Failure(val error: Throwable?) : ExtendedResult<Nothing>()
    object Loading : ExtendedResult<Nothing>()

    companion object {

        fun <T> from(result: Result<T>): ExtendedResult<T> {
            return result.fold(
                onSuccess = { data ->
                    Success(data)
                },
                onFailure = { failure ->
                    failure.printStackTrace()
                    Failure(failure)
                }
            )
        }

        /**
         * Maps an [kotlin.Result](result) of type T to an [ExtendedResult] of type R.
         * @param result the result that should be transformed.
         * @param transform the transformation function.
         */
        fun <T, R> map(result: Result<T>, transform: (T) -> R): ExtendedResult<R> {
            return result.fold(
                onSuccess = { data ->
                    Success(transform(data))
                },
                onFailure = { failure ->
                    failure.printStackTrace()
                    Failure(failure)
                }
            )
        }

    }

}

inline fun <S> ExtendedResult<S>.fold(
    onSuccess: ((S) -> Unit),
    onFailure: ((Throwable?) -> Unit),
    noinline onLoading: ((() -> Unit)?)
) {
    when (this) {
        is ExtendedResult.Success -> onSuccess(value)
        is ExtendedResult.Failure -> onFailure(error)
        is ExtendedResult.Loading -> onLoading?.invoke()
    }
}