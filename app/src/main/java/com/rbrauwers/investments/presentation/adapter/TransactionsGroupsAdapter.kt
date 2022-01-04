package com.rbrauwers.investments.presentation.adapter

import android.content.res.Resources
import android.graphics.Rect
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.rbrauwers.csv.reader.domain.model.Transaction
import com.rbrauwers.investments.R
import com.rbrauwers.investments.databinding.VhTotalBinding
import com.rbrauwers.investments.databinding.VhTransactionBinding
import com.rbrauwers.investments.databinding.VhTransactionsGroupBinding
import com.rbrauwers.investments.domain.model.TransactionsGroup
import com.rbrauwers.investments.util.formatUSD

internal class TransactionsGroupsAdapter(resources: Resources) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val flattenList = mutableListOf<Any>()
    private val evenLineBGColor = ResourcesCompat.getColor(resources, R.color.even_bg, null)
    private val oddLineBGColor = ResourcesCompat.getColor(resources, R.color.odd_bg, null)

    // TODO Should not be placed here
    val decoration = object : RecyclerView.ItemDecoration() {
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

            TOTAL -> {
                val binding = VhTotalBinding.inflate(
                    LayoutInflater
                        .from(parent.context), parent, false
                )

                TotalViewHolder(binding)
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

                val color = if (position % 2 == 0) evenLineBGColor else oddLineBGColor
                vh.bind(transaction, color)
            }

            is TotalViewHolder -> {
                val total = flattenList[position] as? TransactionsTotal ?: run {
                    println("Panic: invalid item")
                    return
                }

                vh.bind(total)
            }
        }
    }

    override fun getItemCount(): Int {
        return flattenList.size
    }

    override fun getItemViewType(position: Int): Int {
        return when (flattenList[position]) {
            is ExpandableTransactionsGroup -> GROUP
            is Transaction -> TRANSACTION
            is TransactionsTotal -> TOTAL
            else -> throw IllegalArgumentException("Panic: invalid item")
        }
    }

    fun submit(transactionsGroups: Set<TransactionsGroup>) {
        flattenList.clear()

        transactionsGroups.forEach {
            val expandableGroup = ExpandableTransactionsGroup(it)
            flattenList.add(expandableGroup)

            if (expandableGroup.isExpanded) {
                flattenList.addAll(expandableGroup.transactionsGroup.transactions)
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
            val baseIndex = position + 1
            val transactionsCount = group.transactionsGroup.transactions.size

            flattenList.addAll(baseIndex, group.transactionsGroup.transactions)
            flattenList.add(
                baseIndex + transactionsCount,
                TransactionsTotal(group.transactionsGroup)
            )
        } else {
            flattenList.removeAll(group.transactionsGroup.transactions)
            flattenList.removeIf {
                (it as? TransactionsTotal)?.transactionsGroup == group.transactionsGroup
            }
        }

        // TODO: use Diff
        notifyDataSetChanged()
    }

    companion object {
        private const val GROUP = 1
        private const val TOTAL = 2
        private const val TRANSACTION = 3
    }

}

private class TransactionsGroupViewHolder(private val binding: VhTransactionsGroupBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(transactionsGroup: TransactionsGroup) {
        binding.dateTextView.text = transactionsGroup.date
        binding.productTextView.text = transactionsGroup.product.name
    }

}

private class TotalViewHolder(private val binding: VhTotalBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(total: TransactionsTotal) {
        binding.valueTextView.text = total.transactionsGroup.total.formatUSD()
    }

}

private data class ExpandableTransactionsGroup(
    val transactionsGroup: TransactionsGroup,
    var isExpanded: Boolean = false
)

private data class TransactionsTotal(val transactionsGroup: TransactionsGroup)