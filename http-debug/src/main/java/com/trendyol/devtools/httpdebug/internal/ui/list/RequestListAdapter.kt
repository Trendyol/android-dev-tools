package com.trendyol.devtools.httpdebug.internal.ui.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.devtools.httpdebug.databinding.DevToolsHttpDebugItemListBinding
import com.trendyol.devtools.httpdebug.internal.model.RequestResponse

internal class RequestListAdapter(private val onRequestClicked: (String) -> Unit) :
    ListAdapter<RequestResponse, RequestListAdapter.ViewHolder>(ListItemDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(DevToolsHttpDebugItemListBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: DevToolsHttpDebugItemListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onRequestClicked.invoke(getItem(adapterPosition).id)
            }
        }

        fun bind(item: RequestResponse) {
            with(binding) {
                devToolsHttpDebugTextMethod.text = item.request.request.method
                devToolsHttpDebugTextUrl.text = item.request.request.url.toString()
                devToolsHttpDebugTextResponseCode.text = item.response?.response?.code?.toString()
                devToolsHttpDebugTextResponseCode.visibility = item.getResponseCodeVisibility()
            }
        }
    }
}
