package com.trendyol.android.devtools.analyticslogger.internal.ui

import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.android.devtools.analyticslogger.databinding.AnalyticsLoggerItemEventBinding
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event
import com.trendyol.android.devtools.analyticslogger.internal.factory.ColorFactory

internal class EventAdapter : PagingDataAdapter<Event, EventAdapter.EventViewHolder>(
    diffCallback = object : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem.uid == newItem.uid
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return oldItem == newItem
        }
    }
) {

    var onItemSelected: ((event: Event) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            AnalyticsLoggerItemEventBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            )
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class EventViewHolder(
        private val binding: AnalyticsLoggerItemEventBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private var boundItem: Event? = null

        init {
            itemView.setOnClickListener {
                boundItem?.let { item -> onItemSelected?.invoke(item) }
            }
        }

        fun bind(event: Event) = with(binding) {
            boundItem = event

            textViewKey.text = event.key
            textViewValue.text = event.value
            textViewPlatform.text = event.platform
            textViewDate.text = event.date
            textViewPlatform.background = createPlatformBackground(event.platform)
        }

        private fun createPlatformBackground(platform: String?): GradientDrawable {
            return GradientDrawable().apply {
                cornerRadius = 20f
                color = ColorStateList.valueOf(
                    ColorFactory.getColor(platform.orEmpty())
                )
            }
        }
    }
}
