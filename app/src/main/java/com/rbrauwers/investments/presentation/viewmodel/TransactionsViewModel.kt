package com.rbrauwers.investments.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.investments.domain.usecase.GetTransactionsUseCase
import com.rbrauwers.investments.util.ExtendedResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class TransactionsViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase
) : ViewModel() {

    private val _transactionsFlow =
        MutableStateFlow<ExtendedResult<List<Transaction>>>(ExtendedResult.Loading)
    val transactionsFlow: StateFlow<ExtendedResult<List<Transaction>>> = _transactionsFlow

    init {
        getTransactions()
    }

    private fun getTransactions() {
        // TODO: dispatcher should a property
        viewModelScope.launch(Dispatchers.IO) {
            getTransactionsUseCase(GetTransactionsUseCase.Params()).onEach {
                _transactionsFlow.value = it
            }.launchIn(this)
        }
    }

}