package com.trendyol.android.devtools.debugmenu.internal.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.AsyncDifferConfig
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.trendyol.android.devtools.debugmenu.DebugActionItem
import com.trendyol.android.devtools.debugmenu.R
import com.trendyol.android.devtools.debugmenu.databinding.DebugMenuItemBinding

internal class DebugMenuAdapter : ListAdapter<DebugActionItem, DebugMenuAdapter.DebugMenuItemViewHolder>(
    AsyncDifferConfig.Builder(DebugMenuItemDiffCallback()).build(),
) {

    lateinit var debugActionItemClickListener: ((Int) -> Unit)
    lateinit var debugActionItemSwitchChangedListener: ((Int, Boolean) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DebugMenuItemViewHolder {
        val binding = DebugMenuItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DebugMenuItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DebugMenuItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class DebugMenuItemViewHolder constructor(
        private val binding: DebugMenuItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var debugActionItem: DebugActionItem

        init {
            binding.debugMenuSwitch.setOnCheckedChangeListener { _, isChecked ->
                if ((debugActionItem as? DebugActionItem.Switchable)?.lastState == isChecked.not()) {
                    debugActionItemSwitchChangedListener(absoluteAdapterPosition, isChecked)
                }
            }
            binding.root.setOnClickListener {
                debugActionItemClickListener(absoluteAdapterPosition)
            }
        }

        fun bind(debugActionItem: DebugActionItem) {
            this.debugActionItem = debugActionItem
            with(binding) {
                debugMenuTextTitle.text = debugActionItem.text
                debugMenuTextDescription.text = debugActionItem.description
                debugMenuImageIcon.setImageResource(debugActionItem.iconDrawableRes)
                debugMenuSwitch.isVisible = debugActionItem is DebugActionItem.Switchable
                if (debugActionItem is DebugActionItem.Switchable &&
                    debugMenuSwitch.isChecked xor debugActionItem.lastState
                ) {
                    debugMenuSwitch.isChecked = debugActionItem.lastState
                }

                with(binding.debugMenuCardIcon) {
                    val color = context
                        .resources
                        .getIntArray(R.array.debug_menu_array_item_color)[absoluteAdapterPosition % COLOR_COUNT]

                    setCardBackgroundColor(color)
                }
            }
        }
    }

    companion object {

        private const val COLOR_COUNT = 7
    }
}
