package com.trendyol.devtools.httpdebug.internal.ui.list

import androidx.recyclerview.widget.DiffUtil
import com.trendyol.devtools.httpdebug.internal.model.RequestResponse

internal object ListItemDiffCallback : DiffUtil.ItemCallback<RequestResponse>() {

    override fun areItemsTheSame(oldItem: RequestResponse, newItem: RequestResponse): Boolean =
        oldItem == newItem

    override fun areContentsTheSame(oldItem: RequestResponse, newItem: RequestResponse): Boolean =
        oldItem.id == newItem.id
}
