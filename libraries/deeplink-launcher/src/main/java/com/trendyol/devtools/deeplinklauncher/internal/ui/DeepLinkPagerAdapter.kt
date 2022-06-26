package com.trendyol.devtools.deeplinklauncher.internal.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.trendyol.devtools.deeplinklauncher.internal.ui.list.DeepLinkListFragment

class DeepLinkPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = TAB_COUNT

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            HISTORY_INDEX -> DeepLinkListFragment.newInstance(position)
            APPS_DEEPLINKS_INDEX -> DeepLinkListFragment.newInstance(position)
            else -> throw IllegalArgumentException("Undefined tab index!")
        }
    }

    companion object {
        private const val TAB_COUNT = 2
        private const val HISTORY_INDEX = 0
        private const val APPS_DEEPLINKS_INDEX = 1
    }
}
