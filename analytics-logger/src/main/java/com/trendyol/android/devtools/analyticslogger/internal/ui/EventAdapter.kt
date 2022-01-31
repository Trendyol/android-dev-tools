package com.trendyol.android.devtools.analyticslogger.internal.ui

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.android.devtools.analyticslogger.R
import com.trendyol.android.devtools.analyticslogger.internal.domain.model.Event

class EventAdapter : PagingDataAdapter<Event, EventAdapter.EventViewHolder>(
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
            LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        )
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class EventViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

        private var boundItem: Event? = null

        private val textViewKey: TextView by lazy { view.findViewById(R.id.textViewKey) }

        private val textViewValue: TextView by lazy { view.findViewById(R.id.textViewValue) }

        private val textViewPlatform: TextView by lazy { view.findViewById(R.id.textViewPlatform) }

        private val textViewDate: TextView by lazy { view.findViewById(R.id.textViewDate) }

        init {
            itemView.setOnClickListener {
                boundItem?.let { item -> onItemSelected?.invoke(item) }
            }
        }

        fun bind(event: Event) {
            boundItem = event

            textViewKey.text = event.key
            textViewValue.text = event.value
            textViewPlatform.text = event.platform?.title.orEmpty()
            textViewDate.text = event.date

            textViewPlatform.background = createPlatformBackground(
                Color.parseColor(event.platform?.color.orEmpty())
            )
        }

        private fun createPlatformBackground(@ColorInt colorInt: Int): GradientDrawable {
            return GradientDrawable().apply {
                cornerRadius = 20f
                color = ColorStateList.valueOf(colorInt)
            }
        }
    }
}
