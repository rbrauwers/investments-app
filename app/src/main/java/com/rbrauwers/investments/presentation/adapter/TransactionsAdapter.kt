package com.rbrauwers.investments.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.investments.databinding.VhTransactionBinding

internal class TransactionsAdapter :
    ListAdapter<Transaction, TransactionViewHolder>(TransactionDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionViewHolder {
        val binding = VhTransactionBinding.inflate(
            LayoutInflater
                .from(parent.context), parent, false
        )

        return TransactionViewHolder(binding)
    }

    override fun onBindViewHolder(vh: TransactionViewHolder, position: Int) {
        vh.bind(getItem(position))
    }

}

internal class TransactionViewHolder(private val binding: VhTransactionBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(transaction: Transaction) {
        binding.dateTextView.text = transaction.date
        binding.productTextView.text = transaction.product.name
    }

}

private object TransactionDiffCallback : DiffUtil.ItemCallback<Transaction>() {
    override fun areItemsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return false
    }

    override fun areContentsTheSame(oldItem: Transaction, newItem: Transaction): Boolean {
        return false
    }
}