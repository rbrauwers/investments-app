package com.rbrauwers.investments.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rbrauwers.investments.domain.model.TransactionsGroup
import com.rbrauwers.investments.domain.usecase.GetTransactionsGroupsUseCase
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
internal class TransactionsGroupsViewModel @Inject constructor(
    private val getTransactionsGroupsUseCase: GetTransactionsGroupsUseCase
) : ViewModel() {

    private val _transactionsGroupsFlow =
        MutableStateFlow<ExtendedResult<Set<TransactionsGroup>>>(ExtendedResult.Loading)
    val transactionsGroupsFlow: StateFlow<ExtendedResult<Set<TransactionsGroup>>> = _transactionsGroupsFlow

    init {
        getTransactions()
    }

    private fun getTransactions() {
        // TODO: dispatcher should a property
        viewModelScope.launch(Dispatchers.IO) {
            getTransactionsGroupsUseCase(GetTransactionsGroupsUseCase.Params()).onEach {
                _transactionsGroupsFlow.value = it
            }.launchIn(this)
        }
    }

}