package com.rbrauwers.investments.presentation.adapter

import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.investments.databinding.VhTransactionBinding
import com.rbrauwers.investments.databinding.VhTransactionsGroupBinding
import com.rbrauwers.investments.domain.model.TransactionsGroup

internal class TransactionsGroupsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var expandableTransactionsGroups = emptyList<ExpandableTransactionsGroup>()
    private val flattenList = mutableListOf<Any>()

    // TODO Should not be placed here
    val decoration = object: RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val index = parent.getChildLayoutPosition(view)
            val left = when (flattenList.getOrNull(index)) {
                is Transaction -> 64 // TODO: calc DP
                else -> 0
            }

            outRect.set(left, 0, 0, 0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            GROUP -> {
                val binding = VhTransactionsGroupBinding.inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )

                TransactionsGroupViewHolder(binding).apply {
                    itemView.setOnClickListener {
                        toggleTransactionsVisibility(bindingAdapterPosition)
                    }
                }
            }

            TRANSACTION -> {
                val binding = VhTransactionBinding.inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )

                TransactionViewHolder(binding)
            }

            else -> {
                throw IllegalArgumentException("Invalid view type")
            }
        }
    }

    override fun onBindViewHolder(vh: RecyclerView.ViewHolder, position: Int) {
        when (vh) {
            is TransactionsGroupViewHolder -> {
                val group = flattenList[position] as? ExpandableTransactionsGroup ?: run {
                    println("Panic: invalid item")
                    return
                }

                vh.bind(group.transactionsGroup)
            }

            is TransactionViewHolder -> {
                val transaction = flattenList[position] as? Transaction ?: run {
                    println("Panic: invalid item")
                    return
                }

                vh.bind(transaction)
            }
        }
    }

    override fun getItemCount(): Int {
        return flattenList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when(flattenList[position]) {
            is ExpandableTransactionsGroup -> GROUP
            is Transaction -> TRANSACTION
            else -> throw IllegalArgumentException("Panic: invalid item")
        }
    }

    fun submit(transactionsGroups: Set<TransactionsGroup>) {
        expandableTransactionsGroups = transactionsGroups.map {
            ExpandableTransactionsGroup(it)
        }

        flattenList.clear()

        expandableTransactionsGroups.forEach {
            flattenList.add(it)
            if (it.isExpanded) {
                flattenList.addAll(it.transactionsGroup.transactions)
            }
        }

        // TODO: use Diff
        notifyDataSetChanged()
    }

    private fun toggleTransactionsVisibility(position: Int) {
        println("toggleTransactionsVisibility $position")

        val group = flattenList[position] as? ExpandableTransactionsGroup ?: run {
            println("Panic: invalid toggle position")
            return
        }

        group.isExpanded = !group.isExpanded

        if (group.isExpanded) {
            flattenList.addAll(position + 1, group.transactionsGroup.transactions)
        } else {
            flattenList.removeAll(group.transactionsGroup.transactions)
        }

        // TODO: use Diff
        notifyDataSetChanged()
    }

    companion object {
        private const val GROUP = 1
        private const val TRANSACTION = 2
    }

}

internal class TransactionsGroupViewHolder(private val binding: VhTransactionsGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(transactionsGroup: TransactionsGroup) {
        binding.dateTextView.text = transactionsGroup.date
        binding.productTextView.text = transactionsGroup.product.name
    }

}

internal data class ExpandableTransactionsGroup(
    val transactionsGroup: TransactionsGroup,
    var isExpanded: Boolean = false
)