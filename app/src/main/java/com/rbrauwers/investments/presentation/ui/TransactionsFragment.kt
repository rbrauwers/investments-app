package com.rbrauwers.investments.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.rbrauwers.investments.databinding.FragmentTransactionsBinding
import com.rbrauwers.investments.presentation.adapter.TransactionsAdapter
import com.rbrauwers.investments.presentation.viewmodel.TransactionsViewModel
import com.rbrauwers.investments.util.fold
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
internal class TransactionsFragment : Fragment() {

    private var binding: FragmentTransactionsBinding? = null
    private val viewModel: TransactionsViewModel by viewModels()
    private val args: TransactionsFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getTransactions(args.filter)
        collect()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentTransactionsBinding.inflate(inflater, container, false)
            .apply {
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
            recyclerView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            recyclerView.adapter = TransactionsAdapter()
        }
    }

    private fun collect() {
        lifecycleScope.launchWhenStarted {
            viewModel.transactionsFlow
                .collectLatest { result ->
                    result.fold(
                        onSuccess = { transactions ->
                            getAdapter()?.submitList(transactions)
                        },
                        onFailure = {
                            // TODO
                        },
                        onLoading = {
                            // TODO
                        }
                    )
                }
        }
    }

    private fun getAdapter(): TransactionsAdapter? {
        return binding?.recyclerView?.adapter as? TransactionsAdapter
    }

}