package com.rbrauwers.investments.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.rbrauwers.investments.databinding.FragmentGroupedTransactionsBinding
import com.rbrauwers.investments.presentation.adapter.TransactionsGroupsAdapter
import com.rbrauwers.investments.presentation.viewmodel.TransactionsGroupsViewModel
import com.rbrauwers.investments.util.fold
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
internal class GroupedTransactionsFragment : Fragment() {

    private var binding: FragmentGroupedTransactionsBinding? = null
    private val viewModel: TransactionsGroupsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        collect()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentGroupedTransactionsBinding.inflate(inflater, container, false).apply {
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

            val adapter = TransactionsGroupsAdapter(resources)
            recyclerView.addItemDecoration(adapter.decoration)
            recyclerView.adapter = adapter
        }
    }

    private fun collect() {
        lifecycleScope.launchWhenStarted {
            viewModel.transactionsGroupsFlow
                .collectLatest { result ->
                    result.fold(
                        onSuccess = { transactionsGroups ->
                            getAdapter()?.submit(transactionsGroups)
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

    private fun getAdapter(): TransactionsGroupsAdapter? {
        return binding?.recyclerView?.adapter as? TransactionsGroupsAdapter
    }
}