package com.trendyol.android.devtools.debugmenu.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugmenu.databinding.DevToolsItemDebugMenuBinding

internal class DebugMenuAdapter : ListAdapter<DebugActionItem, DebugMenuAdapter.DebugMenuItemViewHolder>(
    AsyncDifferConfig
        .Builder(
            object : DiffUtil.ItemCallback<DebugActionItem>() {
                override fun areItemsTheSame(oldItem: DebugActionItem, newItem: DebugActionItem): Boolean {
                    return oldItem.text == newItem.text
                }

                override fun areContentsTheSame(oldItem: DebugActionItem, newItem: DebugActionItem): Boolean {
                    return oldItem.text == newItem.text
                }
            }
        )
        .build()
) {

    var debugActionItemClickListener: ((DebugActionItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebugMenuItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = DevToolsItemDebugMenuBinding.inflate(inflater)
        return DebugMenuItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DebugMenuItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DebugMenuItemViewHolder constructor(
        private val binding: DevToolsItemDebugMenuBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.buttonDebugMenuItem.setOnClickListener {
                val item = getItem(bindingAdapterPosition) ?: return@setOnClickListener
                debugActionItemClickListener?.invoke(item)
            }
        }

        fun bind(debugActionItem: DebugActionItem) {
            binding.buttonDebugMenuItem.text = debugActionItem.text
        }
    }
}
