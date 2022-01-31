package com.trendyol.android.devtools.analyticslogger.internal.ui

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event

class EventAdapter : PagingDataAdapter<Event, EventAdapter.EventViewHolder>(
    diffCallback = object : DiffUtil.ItemCallback<Event>() {
        override fun areItemsTheSame(oldItem: Event, newItem: Event): Boolean {
            return false
        }

        override fun areContentsTheSame(oldItem: Event, newItem: Event): Boolean {
            return false
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        return EventViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class EventViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private val textViewKey: TextView by lazy { view.findViewById(R.id.textViewKey) }

        private val textViewValue: TextView by lazy { view.findViewById(R.id.textViewValue) }

        private val textViewPlatform: TextView by lazy { view.findViewById(R.id.textViewPlatform) }

        private val textViewDate: TextView by lazy { view.findViewById(R.id.textViewDate) }

        fun bind(event: Event) {
            textViewKey.text = event.key
            textViewValue.text = event.value
            textViewPlatform.text = "Firebase"
            textViewDate.text = "01:01:01"

            textViewPlatform.background = createPlatformBackground(view.context, R.color.analytics_logger_red_soft)
        }

        private fun createPlatformBackground(context: Context, colorRes: Int): GradientDrawable {
            return GradientDrawable().apply {
                cornerRadius = 20f
                color = ColorStateList.valueOf(ContextCompat.getColor(context, colorRes))
            }
        }
    }
}
