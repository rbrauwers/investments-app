package com.rbrauwers.investments.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rbrauwers.investments.databinding.FragmentMenuBinding
import com.rbrauwers.investments.domain.model.TransactionsFilter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class MenuFragment : Fragment() {

    private var binding: FragmentMenuBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMenuBinding.inflate(inflater, container, false).apply {
            binding = this
            configUI()
        }.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    private fun configUI() {
        binding?.apply {
            statementTransactionsButton.setOnClickListener {
                navToTransactions(TransactionsFilter.STATEMENT)
            }

            exchangeTransactionsButton.setOnClickListener {
                navToTransactions(TransactionsFilter.EXCHANGE)
            }

            forexTransactionsButton.setOnClickListener {
                navToTransactions(TransactionsFilter.FOREX)
            }

            statementGroupedTransactionsButton.setOnClickListener {
                findNavController()
                    .navigate(MenuFragmentDirections.actionMenuFragmentToGroupedTransactionsFragment())
            }
        }
    }

    private fun navToTransactions(filter: TransactionsFilter) {
        findNavController()
            .navigate(MenuFragmentDirections.actionMenuFragmentToTransactionsFragment(filter))
    }

}