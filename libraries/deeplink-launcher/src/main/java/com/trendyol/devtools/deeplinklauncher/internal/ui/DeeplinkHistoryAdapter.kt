package com.trendyol.devtools.deeplinklauncher.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.devtools.deeplinklauncher.databinding.ItemDeeplikHistoryBinding

internal class DeeplinkHistoryAdapter : ListAdapter<String, DeeplinkHistoryAdapter.DeeplinkHistoryViewHolder>(
    AsyncDifferConfig
        .Builder(
            object : DiffUtil.ItemCallback<String>() {
                override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
                    return oldItem == newItem
                }
            }
        )
        .build()
) {
    var onHistoryItemClicked: ((deeplink: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeeplinkHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDeeplikHistoryBinding.inflate(inflater)
        return DeeplinkHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeeplinkHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DeeplinkHistoryViewHolder(
        private val binding: ItemDeeplikHistoryBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var boundItem: String? = null

        init {
            itemView.setOnClickListener {
                boundItem?.let { item -> onHistoryItemClicked?.invoke(item) }
            }
        }

        fun bind(deeplink: String) = with(binding) {
            boundItem = deeplink

            textViewDeeplink.text = deeplink
        }
    }
}
