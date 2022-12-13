package com.trendyol.devtools.deeplinklauncher.internal.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.devtools.deeplinklauncher.databinding.ItemDeeplikHistoryBinding

internal class DeepLinkListAdapter : ListAdapter<String, DeepLinkListAdapter.DeepLinkHistoryViewHolder>(
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeepLinkHistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemDeeplikHistoryBinding.inflate(inflater)
        return DeepLinkHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DeepLinkHistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DeepLinkHistoryViewHolder(
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
            textViewDeepLink.text = deeplink
        }
    }
}
