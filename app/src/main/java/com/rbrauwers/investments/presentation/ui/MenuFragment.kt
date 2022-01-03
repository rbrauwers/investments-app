package com.rbrauwers.investments.presentation.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.rbrauwers.investments.databinding.FragmentMenuBinding

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
            transactionsButton.setOnClickListener {
                findNavController()
                    .navigate(MenuFragmentDirections.actionMenuFragmentToTransactionsFragment())
            }

            groupedTransactionsButton.setOnClickListener {
                findNavController()
                    .navigate(MenuFragmentDirections.actionMenuFragmentToGroupedTransactionsFragment())
            }
        }
    }

}