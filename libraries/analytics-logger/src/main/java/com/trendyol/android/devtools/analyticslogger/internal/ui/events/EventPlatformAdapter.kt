package com.trendyol.android.devtools.analyticslogger.internal.ui.events

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.android.devtools.analyticslogger.databinding.AnalyticsLoggerItemEventPlatformBinding
import com.trendyol.android.devtools.analyticslogger.internal.factory.ColorFactory

class EventPlatformAdapter(
): RecyclerView.Adapter<EventPlatformAdapter.EventPlatformViewHolder>() {

    private val platforms: MutableList<String> = mutableListOf()
    var onItemSelected: ((platform: String) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventPlatformViewHolder {
        return EventPlatformViewHolder(
            AnalyticsLoggerItemEventPlatformBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: EventPlatformViewHolder, position: Int) {
        platforms[position].let { holder.bind(it) }
    }

    override fun getItemCount(): Int {
        return platforms.size
    }

    fun submitData(data: List<String>) {
        platforms.addAll(data)
        notifyDataSetChanged()
    }

    inner class EventPlatformViewHolder(
        private val binding: AnalyticsLoggerItemEventPlatformBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(platform: String) = with(binding) {
            chipPlatform.text = platform
            chipPlatform.chipBackgroundColor = ColorStateList.valueOf(ColorFactory.getColor(platform))

            chipPlatform.setOnClickListener {
                onItemSelected?.invoke(platform)
            }
        }
    }
}
