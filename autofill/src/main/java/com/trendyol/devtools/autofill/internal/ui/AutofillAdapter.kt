package com.trendyol.devtools.autofill.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.devtools.autofill.databinding.ItemDialogAutofillBinding
import com.trendyol.devtools.autofill.databinding.ItemDialogCategoryBinding
import com.trendyol.devtools.autofill.internal.model.ListItem

internal class AutofillAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val items = mutableListOf<ListItem>()

    var onItemClickedListener: ((ListItem) -> Unit)? = null

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is ListItem.Category -> ITEM_TYPE_CATEGORY
            is ListItem.Autofill -> ITEM_TYPE_AUTOFILL
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_TYPE_CATEGORY -> CategoryViewHolder(
                ItemDialogCategoryBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> AutofillViewHolder(
                ItemDialogAutofillBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is CategoryViewHolder -> holder.bind(items[position] as ListItem.Category)
            is AutofillViewHolder -> holder.bind(items[position] as ListItem.Autofill)
        }
    }

    override fun getItemCount(): Int = items.size

    fun updateItems(items: List<ListItem>) {
        val diffCallback = DiffUtil.calculateDiff(DifferCallback(this.items, items))
        this.items.clear()
        this.items.addAll(items)
        diffCallback.dispatchUpdatesTo(this)
    }

    inner class CategoryViewHolder(
        private val binding: ItemDialogCategoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var boundItem: ListItem? = null

        init {
            binding.root.setOnClickListener {
                boundItem?.let {  onItemClickedListener?.invoke(it) }
            }
        }

        fun bind(item: ListItem.Category) {
            boundItem = item
            binding.textViewTitle.text = item.name
        }
    }

    inner class AutofillViewHolder(
        private val binding: ItemDialogAutofillBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private var boundItem: ListItem? = null

        init {
            binding.root.setOnClickListener {
                boundItem?.let {  onItemClickedListener?.invoke(it) }
            }
        }

        fun bind(item: ListItem.Autofill) {
            boundItem = item
            binding.textViewTitle.text = item.name
            binding.textViewExtras.text = item.data.joinToString(separator = ", ")
        }
    }

    inner class DifferCallback(
        private val oldItems: List<ListItem>,
        private val newItems: List<ListItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldItems.size

        override fun getNewListSize(): Int = newItems.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return when {
                oldItem is ListItem.Category && newItem is ListItem.Category -> oldItem.name == newItem.name
                oldItem is ListItem.Autofill && newItem is ListItem.Autofill -> oldItem == newItem
                else -> false
            }
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldItems[oldItemPosition]
            val newItem = newItems[newItemPosition]
            return when {
                oldItem is ListItem.Category && newItem is ListItem.Category -> oldItem.name == newItem.name
                oldItem is ListItem.Autofill && newItem is ListItem.Autofill -> oldItem == newItem
                else -> false
            }
        }
    }

    companion object {
        private const val ITEM_TYPE_CATEGORY = 0
        private const val ITEM_TYPE_AUTOFILL = 1
    }
}
