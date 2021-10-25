package com.trendyol.devtools.autofill.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
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
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
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

    companion object {
        private const val ITEM_TYPE_CATEGORY = 0
        private const val ITEM_TYPE_AUTOFILL = 1
    }
}